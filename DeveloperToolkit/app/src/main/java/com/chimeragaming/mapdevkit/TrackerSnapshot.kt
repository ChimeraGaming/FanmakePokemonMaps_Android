package com.chimeragaming.mapdevkit

data class TrackerSnapshot(
    val mapId: Int,
    val x: Int,
    val y: Int,
    val direction: Int,
    val mapWidth: Int,
    val mapHeight: Int,
    val playerRealX: Float,
    val playerRealY: Float
) {
    companion object {
        fun parse(text: String, expectedGameId: String): TrackerSnapshot? {
            val values = HashMap<String, String>()
            for (line in text.lineSequence()) {
                val separator = line.indexOf('=')
                if (separator > 0) {
                    values[line.substring(0, separator).trim()] = line.substring(separator + 1).trim()
                }
            }
            if (values["tracker_format"] != "1") return null
            if (values["game_id"] != expectedGameId) return null

            val mapId = values["map_id"]?.toIntOrNull() ?: return null
            val x = values["x"]?.toIntOrNull() ?: return null
            val y = values["y"]?.toIntOrNull() ?: return null
            val direction = values["direction"]?.toIntOrNull() ?: 2
            val mapWidth = values["map_width"]?.toIntOrNull() ?: return null
            val mapHeight = values["map_height"]?.toIntOrNull() ?: return null
            if (mapId <= 0 || mapWidth <= 0 || mapHeight <= 0) return null

            val realX = values["player_real_x"]?.toIntOrNull()?.div(1000f) ?: x.toFloat()
            val realY = values["player_real_y"]?.toIntOrNull()?.div(1000f) ?: y.toFloat()
            return TrackerSnapshot(mapId, x, y, direction, mapWidth, mapHeight, realX, realY)
        }
    }
}

