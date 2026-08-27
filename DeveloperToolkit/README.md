# Fanmake Pokémon Maps Developer Toolkit

This toolkit documents the live map and BattleDex integration system used by Fanmake Pokémon Maps 5.2. It is organized so a contributor can start with a small map test, understand the tracker format, then add a complete game integration.

## Start here

For a first test, use [Basic Test App](07_BASIC_TEST_APP.md). It opens a selected game folder, reads the current map number and player position, then displays the matching map with a simple player marker.

For the shortest build path, use [Build the Test APK](12_DEVKIT_BUILD.md). Place the integrated game under `GameSource`, then run `Build Test APK.bat`.

For the complete viewer, use [Full Test App](08_FULL_TEST_APP.md). It adds background polling, document handle recovery, map indexing, animated player movement, game sprites, entities, followers, items, hidden items, quests, and map pack checks.

For a new game, begin with [Adding a Game](02_ADDING_A_GAME.md), then choose a tracker installation method from [Installer Methods](04_INSTALLER_METHODS.md).

After the base tracker and map view work, use [BattleDex](14_BATTLEDEX.md) to report current opposing Pokémon and display the selected game's Pokédex artwork, types, weaknesses, measurements, caught state, and entry text during battles.

## Map integration

[Map Integration](01_MAP_INTEGRATION.md) explains every successful integration method and lists the games using each method.

The complete flow has four parts:

1. Install a tracker in a script location that the game actually loads.
2. Write live state to `ZMapTracker.txt`.
3. Install map images named for their RPG Maker map IDs.
4. Read the tracker on a background thread and draw the current map.

BattleDex extends the same tracker. It displays the current opponent while `battle_active=1`, then restores the live map when the tracker writes `battle_active=0`.

## BattleDex integration

[BattleDex](14_BATTLEDEX.md) documents the tracker fields, enemy record format, engine hooks, opponent filtering, native Pokédex asset lookup, language fallback, sprite loading, Android state changes, settings, previews, and testing requirements.

BattleDex reports living opponents in wild, trainer, scripted, Safari, double, and multi-battles. It does not report the player's Pokémon. A single opponent is displayed without a switch button. When more than one opponent is active, the user can select the next opponent. The tracker must refresh the list after a switch, send-out, transformation, summon, or faint.

Use Pokédex backgrounds, fonts, type graphics, and Pokémon sprites from the selected game folder when they are available. Do not package game-owned assets in the application. Use the generic layout and English metadata fallback only when the game does not provide the required Pokédex assets or text.

## Guides

- [Map Integration](01_MAP_INTEGRATION.md)
- [Adding a Game](02_ADDING_A_GAME.md)
- [Tracker File Format](03_TRACKER_FILE_FORMAT.md)
- [Installer Methods](04_INSTALLER_METHODS.md)
- [Map Pack Format](05_MAP_PACK_FORMAT.md)
- [Entities, Items, and Quests](06_ENTITIES_ITEMS_AND_QUESTS.md)
- [Basic Test App](07_BASIC_TEST_APP.md)
- [Full Test App](08_FULL_TEST_APP.md)
- [Testing Checklist](09_TESTING_CHECKLIST.md)
- [Troubleshooting](10_TROUBLESHOOTING.md)
- [Game Support Matrix](11_GAME_SUPPORT_MATRIX.md)
- [Build the Test APK](12_DEVKIT_BUILD.md)
- [Writing Style](13_WRITING_STYLE.md)
- [BattleDex](14_BATTLEDEX.md)

## Examples

- [Example tracker snapshot](examples/ZMapTracker.example.txt)
- [Small tracker parser](examples/TrackerSnapshot.kt)
- [Small live map view](examples/SimpleLiveMapView.kt)
- [Basic test activity](examples/BasicTestActivity.kt)

## Included test project

The toolkit root is also a standalone Android Studio project. The build script detects either of these layouts:

```text
GameSource/Game Name/Game/files
GameSource/Game Name/files
```

The detected game must already contain a working `ZMapTracker.txt` and a map folder containing `Map001.png` style images. The game is inspected locally and is not bundled into the APK.

The included test project validates live map tracking. It does not reproduce the complete production BattleDex screen. Test BattleDex in the full application after the base tracker passes the map tests.

## Project rules

- Keep each game integration isolated by game ID, preferences name, map folder, and tracker assets.
- Never delete or modify save files.
- Back up a game script archive or loader before changing it.
- Validate the selected folder before installation.
- Perform folder scans, file reads, archive work, and bitmap decoding away from the Android main thread.
- Confirm that a script folder is loaded at runtime. Its presence alone is not proof.
- Report only living opposing battlers in `battle_enemies`; never report the player's party.
- Write `battle_active=0` on every normal, escaped, lost, aborted, and exceptional battle exit.
- Load game-native Pokédex assets from the selected folder; do not copy them into the application package.
- Return normal sprite paths directly. Export a processed sprite only when the source format requires conversion.
- Preserve the game's language and custom Pokédex entries. Use English fallback data only when the game does not provide usable metadata.
- Keep game failures separate from map integration failures.
- Do not distribute game files, paid assets, or other content without permission.

## Source references

The production implementation is in these project files:

- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedMapGameActivity.kt`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedGameConfig.kt`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedTrackerState.kt`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedLiveMapView.kt`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/MapPackInstaller.kt`
- `app/src/main/assets/generated/universal_entity_tracker.rb`
- `PokemonZMap/app/src/main/java/com/chimeragaming/pokemonzmap/BattleDexView.kt`
- `PokemonZMap/app/src/main/java/com/chimeragaming/pokemonzmap/BattleDexEnglish.kt`
- `PokemonUnchosen/installer/pokemon_unchosen/FanmakePokemonMapsTracker.js`
