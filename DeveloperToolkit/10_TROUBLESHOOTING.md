# Troubleshooting

## Waiting for Live Map

Check these in order:

1. Confirm the game-side tracker is loaded.
2. Confirm `ZMapTracker.txt` changes while the player moves.
3. Confirm `tracker_format=1`.
4. Confirm `game_id` matches the Android configuration.
5. Confirm map ID, width, and height are greater than zero.
6. Confirm the viewer checks the correct runtime tracker location.
7. Confirm the matching map image exists.

For RPG Maker MV, also check `www/ZMapTracker.txt`.

## Folder not accepted

Confirm that the selected folder contains the executable and required data folder. If the download uses a wrapper folder, select the inner game folder unless the integration explicitly supports that wrapper.

Do not validate only by folder name. Check required files and release identifiers.

## Tracker installed but never runs

The script location may exist without being loaded.

- Check the game startup log.
- Check for `Using unpacked scripts.`
- Check whether `Main.rb` loads a fixed list.
- Check whether the Mods loader runs before or after game classes.
- Check whether an MV plugin bundle replaces `plugins.js` loading.

## App becomes unresponsive

Move folder validation, recursive scans, tracker reads, archive work, and bitmap decoding off the main thread.

Validate once before polling. Cache folder handles and indexes. Keep only one read in flight.

## Map loads but sprites are missing

Check `player_graphic` and `map_entities`. Confirm the graphic exists under `Graphics/Characters` or `www/img/characters`.

For RPG Maker MV, confirm the character sheet index and encrypted image key. For followers, inspect the specific follower collection used by the game.

## Wrong or duplicate markers

Use the active event page. Ignore erased and transparent events. Deduplicate spriteset characters against map events and follower collections.

Tighten item and quest classification. Broad text matching can mark scenery or ordinary dialogue.

## Map image is wrong

Confirm the image filename uses the RPG Maker map ID, not the map list position or display name. Check that the selected game version uses the same map data as the map pack.

## Tracker works until the file is rewritten

The tracker may delete and recreate `ZMapTracker.txt`, invalidating the Android document handle. Prefer overwriting the same entry. The viewer should also rebind and retry once after a failed read.

## Game crashes but the map app remains active

Record the game error separately. A JoiPlay script or compatibility crash does not automatically mean the map tracker failed.

