package com.chimeragaming.mapdevkit

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : Activity() {
    companion object {
        private const val PICK_GAME_FOLDER = 7301
        private const val TRACKER_FILE = "ZMapTracker.txt"
        private const val POLL_DELAY_MS = 175L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val worker = Executors.newSingleThreadExecutor()
    private val readInFlight = AtomicBoolean(false)
    private lateinit var config: DevKitConfig
    private lateinit var mapView: LiveMapView
    private lateinit var statusPanel: LinearLayout
    private lateinit var statusTitle: TextView
    private lateinit var statusDetail: TextView
    private lateinit var folderButton: Button
    private var gameRoot: DocumentFile? = null
    private var trackerFile: DocumentFile? = null
    private var mapsFolder: DocumentFile? = null
    private var indexedMaps = mutableMapOf<Int, DocumentFile>()
    private var currentMapId = 0
    private var stopped = false

    private val pollTask = object : Runnable {
        override fun run() {
            if (stopped) return
            if (!readInFlight.compareAndSet(false, true)) {
                handler.postDelayed(this, POLL_DELAY_MS)
                return
            }
            worker.execute {
                runCatching { readAndRender() }
                handler.post {
                    readInFlight.set(false)
                    if (!stopped) handler.postDelayed(this, POLL_DELAY_MS)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        config = DevKitConfig.load(this)
        buildScreen()

        val saved = getPreferences(MODE_PRIVATE).getString("game_root", null)
        if (saved == null) {
            showStatus(config.displayName, "Select the extracted game folder to start the live map.", true)
        } else {
            bindFolder(Uri.parse(saved))
        }
    }

    private fun buildScreen() {
        mapView = LiveMapView(this)
        statusTitle = TextView(this).apply {
            setTextColor(Color.WHITE)
            textSize = 26f
            gravity = Gravity.CENTER
            setTypeface(typeface, Typeface.BOLD)
        }
        statusDetail = TextView(this).apply {
            setTextColor(Color.LTGRAY)
            textSize = 16f
            gravity = Gravity.CENTER
        }
        folderButton = Button(this).apply {
            text = "SELECT GAME FOLDER"
            setOnClickListener { chooseFolder() }
        }
        statusPanel = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(dp(32), dp(32), dp(32), dp(32))
            addView(statusTitle, LinearLayout.LayoutParams(-1, -2))
            addView(statusDetail, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(12) })
            addView(folderButton, LinearLayout.LayoutParams(-1, -2).apply { topMargin = dp(24) })
        }
        setContentView(FrameLayout(this).apply {
            setBackgroundColor(Color.BLACK)
            addView(mapView, FrameLayout.LayoutParams(-1, -1))
            addView(statusPanel, FrameLayout.LayoutParams(-1, -1))
        })
    }

    private fun chooseFolder() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
        startActivityForResult(intent, PICK_GAME_FOLDER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode != PICK_GAME_FOLDER || resultCode != RESULT_OK) return
        val uri = data?.data ?: return
        runCatching {
            contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        getPreferences(MODE_PRIVATE).edit().putString("game_root", uri.toString()).apply()
        bindFolder(uri)
    }

    private fun bindFolder(uri: Uri) {
        showStatus("Opening ${config.displayName}", "Checking the selected folder.", false)
        worker.execute {
            val selected = DocumentFile.fromTreeUri(this, uri)
            val resolved = selected?.let { resolveRoot(it) }
            if (resolved == null) {
                handler.post {
                    showStatus(
                        "Folder not accepted",
                        "Select the game root or one wrapper folder containing the game root.",
                        true
                    )
                }
                return@execute
            }
            gameRoot = resolved
            bindRuntimeFiles()
            if (trackerFile == null || mapsFolder == null || indexedMaps.isEmpty()) {
                handler.post {
                    showStatus(
                        "Integration incomplete",
                        "The tracker or configured map folder is missing.",
                        true
                    )
                }
                return@execute
            }
            handler.post {
                showStatus("Waiting for Live Map", "Launch the game and move a few tiles.", false)
                handler.removeCallbacks(pollTask)
                handler.post(pollTask)
            }
        }
    }

    private fun resolveRoot(selected: DocumentFile): DocumentFile? {
        if (matchesRoot(selected)) return selected
        for (child in selected.listFiles()) {
            if (child.isDirectory && matchesRoot(child)) return child
        }
        return null
    }

    private fun matchesRoot(candidate: DocumentFile): Boolean {
        val tracker = findTracker(candidate)
        val maps = candidate.findFile(config.mapFolder)
        return tracker?.isFile == true && maps?.isDirectory == true
    }

    private fun findTracker(root: DocumentFile): DocumentFile? {
        root.findFile("www")?.takeIf { it.isDirectory }?.findFile(TRACKER_FILE)
            ?.takeIf { it.isFile }?.let { return it }
        return root.findFile(TRACKER_FILE)?.takeIf { it.isFile }
    }

    private fun bindRuntimeFiles() {
        val root = gameRoot ?: return
        trackerFile = findTracker(root)
        mapsFolder = root.findFile(config.mapFolder)?.takeIf { it.isDirectory }
        indexedMaps = indexMaps(mapsFolder)
    }

    private fun indexMaps(folder: DocumentFile?): MutableMap<Int, DocumentFile> {
        val result = mutableMapOf<Int, DocumentFile>()
        for (file in folder?.listFiles().orEmpty()) {
            if (!file.isFile || file.length() <= 0L) continue
            val match = Regex("^Map(\\d{3,})\\.(png|webp)$", RegexOption.IGNORE_CASE)
                .matchEntire(file.name.orEmpty()) ?: continue
            val id = match.groupValues[1].toIntOrNull() ?: continue
            val existing = result[id]
            if (existing == null || file.name.orEmpty().endsWith(".png", true)) result[id] = file
        }
        return result
    }

    private fun readAndRender() {
        var tracker = trackerFile ?: return
        var text = readText(tracker)
        if (text.isBlank()) {
            trackerFile = null
            bindRuntimeFiles()
            tracker = trackerFile ?: return
            text = readText(tracker)
        }
        val snapshot = TrackerSnapshot.parse(text, config.gameId) ?: return
        if (snapshot.mapId != currentMapId) {
            val mapFile = indexedMaps[snapshot.mapId]
            if (mapFile == null) {
                val name = String.format(Locale.US, "Map%03d", snapshot.mapId)
                handler.post { showStatus("Map missing", "$name is not in ${config.mapFolder}.", false) }
                return
            }
            val bitmap = contentResolver.openInputStream(mapFile.uri)?.use {
                BitmapFactory.decodeStream(it)
            } ?: return
            currentMapId = snapshot.mapId
            handler.post {
                statusPanel.visibility = View.GONE
                mapView.setMap(bitmap, snapshot)
            }
        } else {
            handler.post {
                statusPanel.visibility = View.GONE
                mapView.updateState(snapshot)
            }
        }
    }

    private fun readText(file: DocumentFile): String = runCatching {
        contentResolver.openInputStream(file.uri)?.bufferedReader()?.use { it.readText() }.orEmpty()
    }.getOrDefault("")

    private fun showStatus(title: String, detail: String, showButton: Boolean) {
        statusTitle.text = title
        statusDetail.text = detail
        folderButton.visibility = if (showButton) View.VISIBLE else View.GONE
        statusPanel.visibility = View.VISIBLE
    }

    override fun onDestroy() {
        stopped = true
        handler.removeCallbacks(pollTask)
        worker.shutdownNow()
        super.onDestroy()
    }

    private fun dp(value: Int): Int = (value * resources.displayMetrics.density).toInt()
}

