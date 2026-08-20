> ⚠️ Back up your saves before installing a new map.
>
> If you receive `App not installed as package conflicts with an existing package` while updating, perform a one-time reinstall. Uninstall versions v2.3 or below, then install v2.4 or newer.

# Fanmake Pokémon Maps

[![Release v4.5.1](https://img.shields.io/badge/release-v4.5.1-8957e5?style=for-the-badge&logo=github)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases)
![JoiPlay Compatible](https://img.shields.io/badge/JoiPlay-compatible-3DDC84?style=for-the-badge&logo=android&logoColor=white)
[![Star this repository](https://img.shields.io/github/stars/ChimeraGaming/FanmakePokemonMaps_Android?style=for-the-badge&logo=github&label=STAR%20THIS%20REPOSITORY&color=f5c542)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android)

Fanmake Pokémon Maps is an Android live-map companion for Pokémon fan games. It follows your position and switches maps as you move between areas.

It was made with dual-screen handhelds such as the AYN Thor in mind, but it also works on compatible Android phones, tablets, and handhelds.

Current release: **4.4.1** (build 49)

## Supported games

| Game | Verified | Custom Markers | All Sprites | Quests |
| --- | :---: | :---: | :---: | :---: |
| Pokemon Africanvs | ✓ | ✓ |  |  |
| Pokémon Añil/Indigo | ✓ | ✓ |  |  |
| Pokémon Conquer the Gauntlet | ✓ | ✓ |  |  |
| Pokémon Decay |  | ✓ |  |  |
| Pokémon Infinite Fusion |  | ✓ |  |  |
| Pokémon Infinite Fusion 2: Hoenn | ✓ | ✓ |  |  |
| Pokémon Insurgence |  | ✓ |  |  |
| Pokémon Nova |  | ✓ |  |  |
| Pokémon Rejuvenation |  | ✓ |  |  |
| Pokémon Rejuvenation Pt.2 - Where Love Lies |  | ✓ |  |  |
| Pokémon Solar Eclipse | x | ✓ |  |  |
| Pokémon Unbreakable Ties |  | ✓ |  |  |
| Pokémon Z | ✓ | ✓ | 5.0 | ✓ |

[Game Downloads](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/blob/main/GAME_DOWNLOADS.md)

## Install

1. Download the latest APK from [GitHub Releases](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases).
2. Open the APK and allow installation if Android asks.
3. Launch **Fanmake Pokémon Maps**.
4. Open **Maps** and select your game.
5. Choose the game's extracted main folder.

The correct folder normally contains `Game.ini`, `Data`, and `Graphics`.

Versions shown on the Download page are the versions tested with the app. Nearby game versions may also work when their maps and core file structure have not changed.

## Using the app

- **Games** lists the games you have already set up. Select one to open its live map.
- **Maps** lists every supported game. Select one to begin setup.
- **Settings** controls fullscreen mode, screen wake, map size, and player marker appearance.
- **Download** contains the available game download and guide links. They are also listed on the [Game Downloads](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/blob/main/GAME_DOWNLOADS.md) page.
- **About** contains the app version, updates, project links, privacy details, and controls.

Long-press a game, or press **Y** with a controller, to view its status. From there you can open it, repair its map setup, choose a different game folder, or remove it from **Games**. Removing a game from the list does not delete the game or its files.

## Controls

| Input | Action |
| --- | --- |
| D-pad | Move |
| A / Enter | Select |
| Y | Game details and repair options |
| B | Back |
| Touch | Select, scroll, or long-press |
| Pinch | Zoom a live map |
| Drag | Pan a zoomed live map |

## What's new in 4.4.1

- Added 16-bit and HD character-selection screens with a separate gender setting for player markers.
- Fixed Markdown formatting on the in-app update page.

## What's new in 4.4

- Added Pokémon Decay and Pokémon Insurgence.
- Added player and collectible markers for Decay, Insurgence, and Conquer the Gauntlet.
- Added a Male or Female default-marker choice that can be changed globally or per game.
- Per-game item and quest controls now override Universal Settings and remember their last choice.
- Fixed Pokémon Z quest information not appearing.
- Added search and typo suggestions to Games, Maps, and Download.
- Menu tabs now reuse their finished pages for faster switching.

## What's new in 4.3

- All supported games now download their matching versioned map pack from GitHub during setup.
- Downloads and map installation use up to eight workers. A 507-map setup dropped from about nine minutes to about 20 seconds on a supported device.
- Complete existing map sets skip the download. Repair intentionally downloads and replaces the maps.
- Removed all bundled map packs, reducing the APK from 325.4 MB to 9.9 MB.

## What's new in 4.2.2

- Item markers keep their proper tile size while zooming.
- The update changelog now uses the complete Release Changelog artwork.
- Added an optional Pokémon Z GitHub map-install test.
- Fresh map setup now uses up to eight simultaneous map writes, while complete existing map sets are skipped immediately.

## What's new in 4.2

- Added a fast local map-count check before opening compressed map packs.
- Setup skips map extraction and image scanning when every expected map is already installed.
- Manual Repair still replaces the packaged maps when a full repair is requested.

## What's new in 4.1

- Added Pokémon Conquer the Gauntlet v1.0.6.
- Added compatible game versions to the Download page.
- Added a larger, scrollable release changelog when an update is found.

## What's new in 4.0

- Reduced the app package from 325.4 MB to 214.4 MB.
- Map files are stored in smaller packs and unpacked during each game's setup.
- Existing valid maps are skipped. Missing or damaged maps are restored without creating duplicate files.
- Every supported game now includes its complete map pack.
- English Pokémon Indigo and Spanish Pokémon Añil automatically use their matching map packs.

## Updates

The app checks GitHub for a newer release when it opens. It will always ask before downloading an update. You can also check manually from **About**.

Android will still show its normal installation screen before an update is installed.

## Request a game map

Use **Request Game Map** at the top of the Maps page, or submit a request through [GitHub Issues](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/issues).

## Privacy

Game folders and settings stay on your device. They are not uploaded. Internet access is used only for update checks and links that you choose to open.

## Disclaimer

Fanmake Pokémon Maps is an unofficial fan-made companion. It is not affiliated with or endorsed by Nintendo, Game Freak, Creatures Inc., The Pokémon Company, or the developers of the supported fan games. Pokémon and related trademarks belong to their respective owners.
