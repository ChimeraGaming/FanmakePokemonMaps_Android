# Map Integration

## How a live map works

A game-side tracker writes `ZMapTracker.txt`. The Android map viewer reads that file, opens the map image matching `map_id`, then draws the player and any reported entities at their map coordinates.

The minimum working tracker reports:

- Tracker format
- Unique game ID
- Current map ID
- Player tile position
- Player direction
- Map width and height in tiles

The complete 5.0 tracker can also report smooth movement coordinates, animation frame, player graphic, NPCs, Pokémon, followers, objects, effects, items, hidden items, collectibles, legendary encounters, and quests.

## Successful integration methods

### Loose Data scripts

Install the tracker under:

```text
Data/Scripts/998_PokemonMaps/001_PokemonMapsTracker.rb
```

Use this when the game loads loose Ruby files from `Data/Scripts`.

Games using this method:

- Pokémon Añil or Indigo, for compatible loose script releases
- Pokémon Infinite Fusion
- Pokémon Infinite Fusion 2: Hoenn
- Pokémon Nova

### Compiled Scripts archive patch

Read `Data/Scripts.rxdata`, insert the tracker script, then write a patched archive. Back up the original archive first.

Games using this method:

- Pokémon Africanvs
- Pokémon Añil or Indigo, for compatible compiled releases
- PokéMortals
- Pokémon Rejuvenation Pt.2 - Where Love Lies
- Pokémon Solar Eclipse

PokéMortals also needs follower support for the `$game_temp.followers` collection. Older Essentials games may use `$PokemonTemp.dependentEvents` instead.

### Patch Mods

Install the tracker at:

```text
patch/Mods/PokemonMapsTracker.rb
```

Games using this method:

- Pokémon Conquer the Gauntlet
- Pokémon Insurgence
- Pokémon Rejuvenation
- Pokémon Unbreakable Ties

Do not assume that every MKXP or JoiPlay game loads this folder. Test it.

### Root Mods

Install the tracker at:

```text
Mods/PokemonMapsTracker.rb
```

Game using this method:

- Pokémon Soulstones 2: Time Wardens

Time Wardens loads its source tree first and loads the root Mods folder after the game classes exist. The tracker must preserve the same `ZMapTracker.txt` file entry while updating it so Android document handles remain valid.

### Unpacked scripts with a Main loader

Install the tracker at:

```text
Data/UnpackedScripts/PokemonMapsTracker.rb
```

Add a loader line to `Data/UnpackedScripts/Main.rb`. Back up `Main.rb` first and do not add the loader twice.

Game using this method:

- Pokémon Decay

JoiPlay can log `Using unpacked scripts.` when this mode is active. In that case, changing `Scripts.rxdata` has no effect.

### Packaged compiled archive replacement

Install a prepared and verified `Scripts.rxdata` only for the exact game release it was built for. Back up the existing archive.

Game using this method:

- Pokémon Z

Use this only for the exact verified game release because it replaces the selected archive instead of patching it.

### RPG Maker MV plugin

Copy the tracker to:

```text
www/js/plugins/FanmakePokemonMapsTracker.js
```

Register it in `www/js/plugins.js`. If the game replaces the normal plugin loader with a bundle, add a tracker script tag to `www/index.html` before `js/main.js`.

Game using this method:

- Pokémon Unchosen

Unchosen uses a revisioned tracker filename because some JoiPlay local servers cache scripts aggressively. Its runtime tracker may be written to `www/ZMapTracker.txt` instead of the selected game root.

## Folder validation

A normal RPG Maker XP root contains the executable and `Data`. A normal RPG Maker MV root contains the executable and `www`.

Some downloads contain one wrapper folder around the real game. The installer can accept that wrapper only when it contains one recognizable game root. Pokémon Unchosen packages may place the real game inside a folder named `Game`.

Validate required paths, file contents, and release identifiers. Do not accept a folder only because its name looks correct.

## Runtime file locations

The normal tracker location is:

```text
ZMapTracker.txt
```

The RPG Maker MV fallback is:

```text
www/ZMapTracker.txt
```

The map folder is stored in the selected game root. Each game has a unique folder name so integrations do not share state accidentally.

## Runtime rules

1. Resolve and validate the selected root on a background thread.
2. Bind the tracker file and map folder once.
3. Index all map images once.
4. Start tracker polling only after validation finishes.
5. Rebind the tracker file if a read fails because the game replaced the file entry.
6. Keep the last valid same-map entity snapshot during a split tracker write.
7. Update Android views only on the main thread.

Do not scan the complete document tree during every tracker update. Repeated document provider work can block the app and produce an application not responding message.

## Shiny starter setting

The map app writes optional game-side settings to:

```text
FanmakePokemonMapsConfig.txt
```

The current setting is:

```text
force_shiny_starter=1
```

The value `1` enables the starter choice. The value `0` leaves the game's normal starter flow unchanged.
