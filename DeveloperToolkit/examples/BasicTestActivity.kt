package example.livemap

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.documentfile.provider.DocumentFile
import java.util.Locale
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

class BasicTestActivity : Activity() {
    companion object {
        private const val PICK_GAME_FOLDER = 7001
        private const val EXPECTED_GAME_ID = "pokemon_example"
        private const val MAP_FOLDER = "PokemonExampleMaps"
        private const val TRACKER_FILE = "ZMapTracker.txt"
        private const val POLL_DELAY_MS = 250L
    }

    private val handler = Handler(Looper.getMainLooper())
    private val reader = Executors.newSingleThreadExecutor()
    private val readInFlight = AtomicBoolean(false)
    private lateinit var mapView: SimpleLiveMapView
    private var trackerFile: DocumentFile? = null
    private var mapsFolder: DocumentFile? = null
    private var currentMapId = 0
    private var stopped = false

    private val pollTask = object : Runnable {
        override fun run() {
            if (stopped) return
            if (!readInFlight.compareAndSet(false, true)) {
                handler.postDelayed(this, POLL_DELAY_MS)
                return
            }
            reader.execute {
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
        mapView = SimpleLiveMapView(this)
        setContentView(mapView)

        val savedUri = getPreferences(MODE_PRIVATE).getString("game_root", null)
        if (savedUri == null) {
            chooseFolder()
        } else {
            bindFolder(Uri.parse(savedUri))
        }
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
        contentResolver.takePersistableUriPermission(uri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
        getPreferences(MODE_PRIVATE).edit().putString("game_root", uri.toString()).apply()
        bindFolder(uri)
    }

    private fun bindFolder(uri: Uri) {
        reader.execute {
            val root = DocumentFile.fromTreeUri(this, uri)
            val tracker = root?.findFile(TRACKER_FILE)
            val maps = root?.findFile(MAP_FOLDER)
            if (tracker?.isFile != true || maps?.isDirectory != true) {
                handler.post {
                    Toast.makeText(this, "Tracker or map folder is missing.", Toast.LENGTH_LONG).show()
                    chooseFolder()
                }
                return@execute
            }
            trackerFile = tracker
            mapsFolder = maps
            handler.post {
                handler.removeCallbacks(pollTask)
                handler.post(pollTask)
            }
        }
    }

    private fun readAndRender() {
        val tracker = trackerFile ?: return
        val text = contentResolver.openInputStream(tracker.uri)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: return
        val snapshot = TrackerSnapshot.parse(text, EXPECTED_GAME_ID) ?: return

        if (snapshot.mapId != currentMapId) {
            val name = String.format(Locale.US, "Map%03d.png", snapshot.mapId)
            val file = mapsFolder?.findFile(name) ?: return
            val bitmap = contentResolver.openInputStream(file.uri)?.use {
                BitmapFactory.decodeStream(it)
            } ?: return
            currentMapId = snapshot.mapId
            handler.post { mapView.showMap(bitmap, snapshot) }
        } else {
            handler.post { mapView.updatePlayer(snapshot) }
        }
    }

    override fun onDestroy() {
        stopped = true
        handler.removeCallbacks(pollTask)
        reader.shutdownNow()
        super.onDestroy()
    }
}
