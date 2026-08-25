# Build the Test APK

## Recommended path

Use `Build Test APK.bat` for the first build. It detects the integrated game, creates the app configuration, runs Gradle, and copies the APK to:

```text
Output/MapDevKitTest.apk
```

Use Android Studio when changing the app, adding entity rendering, debugging with Logcat, or stepping through tracker parsing.

The Gradle wrapper is included. The first build can download Gradle and Android dependencies when they are not already available on the computer.

## Required game state

The game folder must already contain:

- A working `ZMapTracker.txt` in the game root or `www`
- `tracker_format=1`
- A unique `game_id`
- A map folder directly under the game root
- At least one `Map001.png` style map image

The build script does not create a game-specific tracker or render RPG Maker data. Use the other toolkit guides to complete those parts first.

## Folder layout

Extract the toolkit and place one game under `GameSource`.

Direct layout:

```text
DeveloperToolkit
  GameSource
    Game Name
      Game.exe
      Data
      ZMapTracker.txt
      GameNameMaps
```

Nested layout:

```text
DeveloperToolkit
  GameSource
    Game Name
      Game
        Game.exe
        Data
        ZMapTracker.txt
        GameNameMaps
```

The detector searches the `GameSource` folder and two directory levels below it. It accepts RPG Maker XP roots with `Data` and RPG Maker MV roots with `www`.

## Build with the batch file

Run:

```text
Build Test APK.bat
```

The script:

1. Finds the real game root.
2. Reads `game_id` from the tracker.
3. Detects the map folder.
4. Reads the game title from `Game.ini` when available.
5. Writes `app/src/main/assets/devkit.properties`.
6. Builds the debug APK.
7. Copies the result to `Output/MapDevKitTest.apk`.

## Build with Android Studio

Open the `DeveloperToolkit` folder as an Android Studio project. Run `Build Test APK.bat` once whenever the game source or local configuration changes. After that, Android Studio can build and run the app normally.

## Optional overrides

Copy `devkit.local.properties.example` to `devkit.local.properties` and change only the values that need an override.

```text
display_name=My Game
map_folder=MyGameMaps
```

The local properties file and `GameSource` contents are ignored by Git so private game files and machine-specific values are not committed.

## Android test flow

Install `Output/MapDevKitTest.apk`. On the first launch, select the same integrated game folder on the Android device. The app accepts the game root directly or one wrapper folder around it.

Later launches open directly into the live map while the saved folder permission remains valid.
