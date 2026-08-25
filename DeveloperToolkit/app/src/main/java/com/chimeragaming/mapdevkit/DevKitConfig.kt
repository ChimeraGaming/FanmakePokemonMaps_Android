package com.chimeragaming.mapdevkit

import android.content.Context
import java.util.Properties

data class DevKitConfig(
    val displayName: String,
    val gameId: String,
    val mapFolder: String
) {
    companion object {
        fun load(context: Context): DevKitConfig {
            val values = Properties()
            context.assets.open("devkit.properties").reader(Charsets.UTF_8).use(values::load)
            return DevKitConfig(
                displayName = values.getProperty("display_name", "Pokémon Map Test"),
                gameId = values.getProperty("game_id", "pokemon_example"),
                mapFolder = values.getProperty("map_folder", "PokemonExampleMaps")
            )
        }
    }
}
