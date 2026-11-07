# Adding a Game

## 1. Identify the engine and release

Record the game engine, game version, executable name, `Game.ini` title, and folder structure.

Check for:

- `Data/Scripts`
- `Data/Scripts.rxdata`
- `Data/UnpackedScripts`
- `patch/Mods`
- Root `Mods`
- `www/js/plugins`
- Startup logs that name the script source

## 2. Choose a unique identity

Create a lowercase game ID that will not change between supported releases.

Example:

```text
pokemon_example
```

Also choose unique values for:

- Android preferences name
- Map folder name
- Tracker asset path
- Map pack filename

## 3. Validate the game root

Require stable files that identify the game. Good checks include a specific `Game.ini` title, executable, script archive, version file, or known text inside a release file.

If multiple languages or releases use the same integration, define variants with their own required paths and labels.

Production games use the shared Auto Detect Game Folder flow. New activities based on `GeneratedMapGameActivity` inherit it automatically. Other setup activities must implement `GameFolderDetectionHost` and attach the shared button above the manual picker.

Add known archive and extracted folder names from the game's download pages to `assets/generated/game_folder_detection.json`. These names help order the search. Always validate the game contents before offering a match; a folder name alone is not enough. Include language and release labels when a variant can be identified reliably.

Reuse the saved RPGXP root across games. A single match must ask No or Yes. Multiple matches must offer each copy and Neither. No and Neither open the manual picker at RPGXP. Save the game association only after Yes, Select, or a completed manual selection.

Check the candidate layout for the game's engine. RPG Maker XP and PSDK use `Game.ini`, `Data`, and `Graphics`; RPG Maker MV uses its executable and `www` folder, then validates `www/data/System.json` and its loader. Add candidate checks for any new engine before enabling its integration.

## 4. Prove the tracker load location

Install a harmless tracker that writes a small text file. Launch the game and move several tiles. If the file does not update, the runtime is not loading that script location.

Do this before building map export, entity tracking, or quests.

## 5. Write the base tracker

Start with the required fields from [Tracker File Format](03_TRACKER_FILE_FORMAT.md). Confirm map ID, coordinates, direction, width, and height.

Use the game engine's update callback. Throttle writes enough to avoid unnecessary storage work while keeping movement responsive.

## 6. Add maps

Create images named `Map001.png`, `Map002.png`, and so on. The number must match the RPG Maker map ID.

Package the images as described in [Map Pack Format](05_MAP_PACK_FORMAT.md).

## 7. Add entities and items

Report visible event graphics first. Then classify NPCs, Pokémon, followers, interaction objects, effects, items, and hidden items.

Use [Entities, Items, and Quests](06_ENTITIES_ITEMS_AND_QUESTS.md) for the record layouts.

## 8. Add quests last

Quest markers need game-specific evidence. Prefer quest APIs, switches, variables, and active event page conditions. Avoid matching broad words from ordinary dialogue.

The project utility `tools/audit_quest_triggers.py` can inspect RPG Maker XP data for coordinate-bearing quest triggers.

## 9. Add the Android configuration

Create a `GeneratedGameConfig` with:

- Display name
- Game ID
- Preferences name
- Expected game title
- Variants when needed
- Map folder name
- Tracker asset path
- Installer mode
- Map pack URL, filename, hash, and map count
- Setup revision

Increase the setup revision when Repair must reinstall a changed tracker or integration file.

## 10. Test the full lifecycle

Test a new setup, launch, movement, map transition, app restart, game restart, Repair, and an interrupted tracker write. Confirm that save data remains untouched.

Also test auto detection with a renamed folder, nested extraction, wrong game, multiple releases or languages, cancelled selection, revoked root access, and app restart. Verify No, Yes, Select, and Neither before shipping.

