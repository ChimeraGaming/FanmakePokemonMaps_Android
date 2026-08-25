# Installer Methods

## Method selection

| Runtime structure | Installer mode |
| --- | --- |
| Loose Ruby files under `Data/Scripts` | `DATA_SCRIPTS` |
| Ruby files loaded from `patch/Mods` | `PATCH_MODS` |
| Ruby files loaded from root `Mods` | `ROOT_MODS` |
| JoiPlay extracted scripts with `Main.rb` | `UNPACKED_SCRIPTS` |
| Active `Data/Scripts.rxdata` archive | `COMPILED_SCRIPTS` |
| Exact release with prepared archive | `PACKAGED_COMPILED_SCRIPTS` |
| RPG Maker MV plugin runtime | `MV_PLUGIN` |

## Common installer requirements

- Validate the root before writing.
- Request persisted Android folder permission.
- Back up every archive, loader, plugin list, or HTML file before its first change.
- Make installation repeatable. Repair must not add duplicate loaders or plugin entries.
- Keep every game in its own tracker asset directory.
- Create a placeholder `ZMapTracker.txt` with the correct game ID.
- Create the configured map folder.
- Verify the tracker identity and expected map count after installation.
- Never touch save folders or save files.

## Loose scripts

Create `998_PokemonMaps` and copy the tracker as `001_PokemonMapsTracker.rb`. Keep the high numeric folder prefix so the tracker loads after the main game classes.

## Compiled archive patching

Read every script entry, preserve all non-tracker data, remove or replace an older tracker entry, then write the refreshed archive. Support the integer sizes used by the selected Ruby Marshal format.

Do not install a prepared archive when the selected release is unknown.

## Unpacked scripts

Copy the tracker beside the extracted scripts and add one loader statement to `Main.rb`. A standalone new file is not enough when JoiPlay uses a fixed script list.

## Mods folders

Confirm when the folder is loaded. Some games load Mods before `Game_Player` exists. Others load it after all game classes. Hook installation timing must match that order.

## RPG Maker MV

Create `www/js/plugins` when it is missing. Add one enabled plugin entry to `www/js/plugins.js`.

When the game uses a bundled plugin loader, also insert a direct script tag in `www/index.html`. Use a revisioned script filename when the local web server caches old files.

## Repair behavior

A setup revision should refresh tracker and installer files without replacing hundreds of valid maps. A user-requested full Repair can download a clean map pack and replace damaged maps.

