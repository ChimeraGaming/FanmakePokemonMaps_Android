# Map Integration Methods

This document explains the integration methods that have successfully produced live maps in Fanmake Pokémon Maps. Use it when maintaining an existing game or adding support for another RPG Maker Pokémon game.

## How a live map works

Each supported game needs two parts:

1. A Ruby tracker runs inside the game and writes `ZMapTracker.txt`.
2. The Android app reads that file and displays the matching map image from the game's map folder.

The tracker records the current map ID, player coordinates, direction, map size, map name, visible entities, and item locations when available.

Most supported games use verified map images installed from a map pack. A tracker can also request map generation when a verified image is not available.

## Loose Data scripts

This method installs the tracker as a Ruby file under:

```text
Data/Scripts/998_PokemonMaps/001_PokemonMapsTracker.rb
```

Use this method when the game loads loose scripts from `Data/Scripts`.

Successfully verified games:

- Pokémon Añil, also known as Pokémon Indigo, for releases that use loose scripts
- Pokémon Infinite Fusion
- Pokémon Infinite Fusion 2: Hoenn
- Pokémon Nova

Pokémon Infinite Fusion 2: Hoenn currently has its own activity and installer, but it uses the same loose script principle.

## Compiled Scripts archive patching

This method reads `Data/Scripts.rxdata`, adds the tracker to the Ruby script archive, and writes the patched archive back to the game folder. The original archive is backed up before it is changed.

Use this method when the runtime executes the Ruby bodies stored inside `Scripts.rxdata`.

Successfully verified games:

- Pokémon Africanvs
- Pokémon Añil, also known as Pokémon Indigo, for releases that use a compiled archive
- Pokémon Solar Eclipse

Test integrations using this method:

- PokéMortals

The Añil installer detects the selected release and chooses loose scripts or compiled archive patching as needed.

The archive patcher supports both normal fixnum script IDs and the bignum IDs used by some Essentials builds.

## Patch Mods

This method installs the tracker at:

```text
patch/Mods/PokemonMapsTracker.rb
```

Use this method when the JoiPlay or MKXP runtime loads Ruby files from `patch/Mods`.

Successfully verified games:

- Pokémon Conquer the Gauntlet
- Pokémon Insurgence
- Pokémon Unbreakable Ties

Do not assume every MKXP game supports this folder. Pokémon Decay has a `patch` folder but does not execute tracker files placed in `patch/Mods`.

## Root Mods folder

This method installs the tracker at:

```text
Mods/PokemonMapsTracker.rb
```

Use this method when a game loads its normal source tree first and then explicitly loads Ruby files from a root-level `Mods` folder.

Test integration using this method:

- Pokémon Soulstones 2: Time Wardens

Time Wardens keeps its source files under `Scripts`. Its small `Scripts.rxdata` file loads that tree, and its final main script loads the root-level `Mods` folder after the game classes are defined. Installing the tracker there avoids changing the loader archive and ensures `Game_Player` exists before the tracker extends it.

## Unpacked scripts with a Main loader

Some JoiPlay configurations extract the compiled archive into `Data/UnpackedScripts` and log `Using unpacked scripts.` In this mode, edits to `Scripts.rxdata` are ignored during play.

The installer performs these steps:

1. Copies the tracker to `Data/UnpackedScripts/PokemonMapsTracker.rb`.
2. Creates `Main.rb.fanmakepokemonmaps_backup` if a backup does not already exist.
3. Adds a loader line to the start of `Data/UnpackedScripts/Main.rb`.
4. Leaves the loader unchanged when it is already installed.

Successfully verified game:

- Pokémon Decay

This method fixed Decay because JoiPlay used a fixed list of unpacked script names. Adding a new Ruby file alone was not enough. The already registered `Main.rb` had to load the tracker explicitly.

## Packaged compiled archive replacement

This method installs a prepared `Scripts.rxdata` supplied with the app. The existing game archive is backed up first.

Use this method only when the prepared archive is tied to a known and verified game release. It is less flexible than patching the selected game's existing archive.

Successfully verified game:

- Pokémon Z

Pokémon Z uses a dedicated installer because it also installs game specific assets and its verified map package.

## RPG Maker MV plugin registration

This method copies the tracker to:

```text
www/js/plugins/FanmakePokemonMapsTracker.js
```

It also adds an enabled entry to `www/js/plugins.js`. The original plugin list
is backed up before it is changed.

Test integration using this method:

- Pokémon Unchosen, English and French v1.6.4

The Unchosen tracker reads RPG Maker MV event pages directly. It reports the
active player character sheet and sheet index, visible events, followers,
items, hidden items, and quest commands.

## Prebuilt map packs

Tracker installation determines how live position data reaches the app. Map packs determine what background image is shown.

Verified map packs are preferred because they provide complete maps immediately and avoid depending on runtime image export. The installer keeps valid existing maps and replaces missing or damaged files. An explicit Repair action can force a full replacement.

The following games have been verified with map images and sprites:

- Pokémon Africanvs
- Pokémon Añil, also known as Pokémon Indigo
- Pokémon Conquer the Gauntlet
- Pokémon Decay
- Pokémon Infinite Fusion
- Pokémon Infinite Fusion 2: Hoenn
- Pokémon Insurgence
- Pokémon Nova
- Pokémon Solar Eclipse
- Pokémon Unbreakable Ties
- Pokémon Z

## Choosing an integration method

Check the game in this order:

1. Read the startup log and look for `Using unpacked scripts.`
2. Check whether `Data/UnpackedScripts` exists and contains the scripts named in the log.
3. Check whether `Data/Scripts` contains loose Ruby files.
4. Check whether the runtime actually executes files placed in `patch/Mods`.
5. Check whether the game has a root-level `Mods` loader that runs after its normal scripts.
6. Check whether `Data/Scripts.rxdata` is the archive used at runtime.
7. Use a packaged archive only for a known release that has been tested with that exact archive.

File existence alone does not prove that a runtime loads a location. Confirm the method by launching the game, moving a few tiles, and checking that `ZMapTracker.txt` receives a current map ID and timestamp.

## Validation checklist

A game is considered successfully integrated when all of these checks pass:

- The game starts normally after tracker installation.
- `ZMapTracker.txt` changes while the player moves.
- The tracker reports a map ID greater than zero.
- The map image matches the area shown in the game.
- The player sprite appears at the correct coordinates.
- Player movement updates without freezing the Android app.
- Reopening the game and map app still works.
- Repair can restore the tracker without deleting save data.

Game errors and map integration errors should be recorded separately. A game can have its own JoiPlay script error while the tracker and map display continue to work correctly.
