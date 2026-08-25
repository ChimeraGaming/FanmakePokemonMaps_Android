package example.livemap

data class TrackerSnapshot(
    val mapId: Int,
    val x: Int,
    val y: Int,
    val direction: Int,
    val mapWidth: Int,
    val mapHeight: Int
) {
    companion object {
        fun parse(text: String, expectedGameId: String): TrackerSnapshot? {
            val values = HashMap<String, String>()
            for (line in text.lineSequence()) {
                val separator = line.indexOf('=')
                if (separator > 0) {
                    val key = line.substring(0, separator).trim()
                    val value = line.substring(separator + 1).trim()
                    values[key] = value
                }
            }

            if (values["tracker_format"] != "1") return null
            if (values["game_id"] != expectedGameId) return null

            val mapId = values["map_id"]?.toIntOrNull() ?: return null
            val x = values["x"]?.toIntOrNull() ?: return null
            val y = values["y"]?.toIntOrNull() ?: return null
            val direction = values["direction"]?.toIntOrNull() ?: 2
            val width = values["map_width"]?.toIntOrNull() ?: return null
            val height = values["map_height"]?.toIntOrNull() ?: return null

            if (mapId <= 0 || width <= 0 || height <= 0) return null
            return TrackerSnapshot(mapId, x, y, direction, width, height)
        }
    }
}

