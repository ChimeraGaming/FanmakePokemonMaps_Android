# Fanmake Pokémon Maps

[![Release v1.2.1](https://img.shields.io/badge/release-v1.2.1-8957e5?style=for-the-badge&logo=github)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases)
![JoiPlay Compatible](https://img.shields.io/badge/JoiPlay-compatible-8957e5?style=for-the-badge&logo=android)

Fanmake Pokémon Maps is an Android live-map companion for supported Pokémon fan games. It reads each game's local tracker data, displays the current map, follows the player's position, and changes maps automatically as the player moves between areas.

The app was designed with dual-screen Android handhelds such as the AYN Thor in mind, but it supports any compatible Android device with touch or controller input.

Current release: **1.2.1** (build 13)

## Supported games

- Pokémon Z
- Pokémon Infinite Fusion 2 :Hoenn

## Install the app

1. Download the latest APK from [GitHub Releases](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases).
2. Allow installation from your browser or file manager if Android asks.
3. Open the APK and install Fanmake Pokémon Maps.
4. Launch the app and open **Maps** from the left sidebar.
5. Select a game and choose its extracted root folder when prompted.

The selected folder must contain `Game.ini`, `Data/`, and `Graphics/`. The app requests persistent read/write access so it can install that game's tracker and map files.

## Using the app

- **Games** shows only games already associated with the app. Selecting one opens its live map.
- **Maps** shows every supported map companion. Selecting an unconfigured game starts its automatic setup.
- **Settings** contains universal display and marker options shared by every game.
- **Download** provides project resources, update checking, and supported-game download or guide pages.
- **About** shows the installed version, project links, update checking, privacy information, and controls.

Long-press a game with touch, or press **Y** with a controller, to open its details. Installed games can be opened, repaired, associated with a different folder, or removed from **My Games**. Removing an association does not delete the game or its files.

## Controls

| Input | Action |
| --- | --- |
| D-pad | Move focus |
| A / Enter | Select or open |
| Y | Open game details and management |
| B | Go back |
| Touch | Select, scroll, or long-press |

## Universal settings

- Keep the screen awake
- Fullscreen mode
- Marker size: Small, Default, or Large
- Marker style: Classic, High contrast, or Minimal
- Map scaling: Fit entire map, Fill screen, or Pixel-perfect

These settings apply across all supported live maps. Each game still owns its own folder, tracker, generated or installed maps, and setup state.

## Updates

The app checks the latest published GitHub release when it opens. If a newer version is available, it asks before opening the release APK or release page. Choosing **Not Now** dismisses the prompt and leaves the app open.

Updates can also be checked manually from **Download** or **About**. The app never installs an update without the user's confirmation and Android's normal package-install approval.

## Privacy

Game-folder access is provided through Android's system folder picker. Tracker data, folder associations, and preferences remain on the device and are not uploaded. Internet access is used for GitHub update checks and for opening project, issue, download, and guide links selected by the user.

## Request another game map

Use the **Request Game Map** action at the top of the Maps page, or open the project's [GitHub Issues](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/issues). The repository includes a map-request issue template.

## Project structure

```text
FanmakePokemonMaps/
├── app/                              Universal menu, settings, registry, and updater
├── PokemonZMap/                      Pokémon Z live map, installer, and assets
├── PokemonInfiniteFusion2Hoenn/      Infinite Fusion 2 :Hoenn module
│   ├── android/                      Android setup and live-map implementation
│   ├── installer/                    Game-side tracker files
│   ├── maps/                         Map-generation documentation and metadata
│   └── tools/                        Game-specific offline tooling
├── tools/                            Release-signing and build helpers
├── CHANGELOG.md                      Release changes
└── RELEASE.md                        Maintainer release procedure
```

Game-specific code and data stay inside each game's folder. The parent `app` module supplies the shared application shell and packages the supported game modules into one APK.

## Disclaimer

Fanmake Pokémon Maps is an unofficial fan-made companion. It is not affiliated with or endorsed by Nintendo, Game Freak, Creatures Inc., The Pokémon Company, or the developers of the supported fan games. Pokémon and related trademarks belong to their respective owners.
