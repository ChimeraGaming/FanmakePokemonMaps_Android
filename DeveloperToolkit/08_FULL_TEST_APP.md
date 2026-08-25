# Full Test App

## Goal

Extend the basic viewer into a production style live map. Keep the same direct launch behavior, then add complete tracker parsing, sprites, entity filters, touch controls, map packs, diagnostics, and recovery.

## Architecture

Use separate responsibilities:

- Game configuration defines identity, variants, map folder, installer, and assets.
- Folder resolver validates the selected release once.
- Installer writes tracker files and maps.
- Tracker reader reads snapshots on one background executor.
- Tracker parser converts text into immutable state.
- Map index resolves map IDs without repeated folder scans.
- Map view draws the image and live markers.
- Settings control marker visibility and viewport behavior.

The production references are listed in the toolkit [README](README.md).

## Background tracker loop

Keep only one tracker read in flight. Schedule the next read after the previous read completes. This prevents a slow document provider from building a queue of stale reads.

Use a fast read interval only when needed. Rendering can run at a slower user-selected movement rate.

Stop callbacks and shut down executors when the activity is destroyed.

## Tracker recovery

Some game trackers replace `ZMapTracker.txt`. A cached Android document handle can then point at a removed entry.

If a read returns empty or fails:

1. Clear the cached tracker handle.
2. Find the runtime tracker again.
3. Retry the read once.

For RPG Maker MV, check `www/ZMapTracker.txt` before the root tracker.

## Split snapshot recovery

A game-specific tracker can write the base fields immediately before an entity extension adds `map_entities`. If the new snapshot is otherwise valid and the previous state is from the same map, preserve the previous entities until the complete snapshot arrives.

This avoids a visible marker flicker.

## Map caching

Index the configured map folder once. Decode a map bitmap only when `map_id` changes. Cache character sprite directory entries and decoded graphics.

Recycle a decoded map only when the view no longer uses it.

## Complete rendering

Add support for:

- Smooth player coordinates
- Walking direction and animation frame
- Current player graphic
- RPG Maker MV character sheet index
- NPCs
- Pokémon
- Followers
- Interaction objects
- HM or Alchemy objects
- Effects
- Items and hidden items
- Legendary graphics
- Quest markers

Give each category its own visibility setting.

## Touch behavior

Support fit, fill, and pixel scaling. Preserve aspect ratio unless the user explicitly enables stretching.

Use pinch zoom and bounded panning. When following the player while zoomed, keep the player centered without moving the map outside its legal bounds.

For a minimap overlay, lock only its screen position. Pinch zoom should change the content viewport without expanding the overlay box.

## Map packs

Download over HTTPS, support interrupted download behavior, verify SHA-256, verify expected map count, and validate every image before installation.

Keep valid existing maps during normal updates. A full Repair should start with a clean verified archive.

## Diagnostics

Expose copyable values for:

- Selected root URI
- Detected game variant
- Tracker location and version
- Current map ID
- Map folder and installed map count
- Last valid tracker time
- Export status and error

Diagnostics should never display or modify save data.

## Completion checks

Test rapid movement, map changes, app switching, game restarts, tracker replacement, lost document handles, a damaged map, and a full Repair. None of these tests should block the main thread.
