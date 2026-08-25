# Basic Test App

## Goal

Build a small Android app that opens directly into a live map after its first folder selection. It displays the current map image and a simple player dot. It does not install trackers or map packs.

Use this app to prove that a game-side tracker writes valid map state.

## Requirements

- Android Studio with Java 17
- Minimum Android SDK 26
- One game with a working `ZMapTracker.txt`
- One map folder containing `Map001.png` style images
- AndroidX DocumentFile

Add this dependency:

```kotlin
implementation("androidx.documentfile:documentfile:1.0.1")
```

## App screens

The basic app needs only two states:

1. Folder selection, shown only when no saved folder permission exists.
2. Live map, shown immediately on later launches.

## First launch

Open Android's folder picker with `ACTION_OPEN_DOCUMENT_TREE`. After the user chooses the game root:

1. Call `takePersistableUriPermission` for read access.
2. Save the tree URI string in SharedPreferences.
3. Validate `ZMapTracker.txt` and the configured map folder on a background thread.
4. Open the live map view.

The test app should not ask for the folder again unless permission is lost or the folder is invalid.

## Read the minimum state

Use [TrackerSnapshot.kt](examples/TrackerSnapshot.kt). The simple parser reads:

- Game ID
- Map ID
- X and Y
- Direction
- Map width and height

Reject a snapshot when the tracker format, game ID, map ID, width, or height is invalid.

## Find the map image

Format the current map ID with at least three digits.

Example:

```text
map_id=7
Map007.png
```

Open the image from the selected game's configured map folder.

## Draw the map and player

Use [SimpleLiveMapView.kt](examples/SimpleLiveMapView.kt). It fits the complete map inside the view without stretching and draws a player dot at the center of the reported tile.

[BasicTestActivity.kt](examples/BasicTestActivity.kt) connects the folder picker, saved folder permission, background tracker reader, map loading, parser, and view. Change `EXPECTED_GAME_ID` and `MAP_FOLDER` for the game being tested.

## Poll safely

Use one background executor. Read the tracker about four times per second for a basic test. Post only the parsed snapshot and decoded map to the main thread.

Do not validate the complete folder during every update. Validate once, keep the tracker and map folder handles, and rebind only after a failed read.

## Suggested activity flow

```text
onCreate
  read saved folder URI
  if missing, show folder picker
  otherwise, validate folder in background
  bind tracker and map folder
  start polling
  load a new image only when map_id changes
  update the player marker when coordinates change
```

## What this proves

The basic app is successful when:

- It opens directly to the live map after the first setup.
- Moving in game moves the dot.
- Entering another map loads the matching image.
- Leaving and reopening the app keeps folder access.
- The app remains responsive while the game writes the tracker.

After these checks pass, continue with [Full Test App](08_FULL_TEST_APP.md).
