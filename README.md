> ⚠️ Back up your saves before installing a new map.

# Fanmake Pokémon Maps

[![Release v5.3](https://img.shields.io/badge/release-v5.3-8957e5?style=for-the-badge&logo=github)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases)
![Android](https://img.shields.io/badge/Android-compatible-3DDC84?style=for-the-badge&logo=android&logoColor=white)
[![Star this repository](https://img.shields.io/github/stars/ChimeraGaming/FanmakePokemonMaps_Android?style=for-the-badge&logo=github&label=STAR%20THIS%20REPOSITORY&color=f5c542)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android)

Fanmake Pokémon Maps is an Android live-map and battle companion for Pokémon fan games. It follows your position, switches maps as you move between areas, and can display the opposing Pokémon's Pokédex information during battles.

It was made with dual-screen handhelds such as the AYN Thor in mind, but it also works on compatible Android phones, tablets, and handhelds.

Current release: **5.3** (build 58)

## Supported games

| Game | Verified | All Sprites | BattleDex | Quests | Shiny Starter |
| --- | :---: | :---: | :---: | :---: | :---: |
| Pokemon Africanvs | ✓ | ✓ | ✓ | ✓ | N/A |
| Pokémon Añil/Indigo | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Conquer the Gauntlet | ✓ | ✓ | ✓ | N/A | |
| Pokémon Decay | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Infinite Fusion | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Infinite Fusion 2: Hoenn | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Insurgence | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Nova | ✓ | ✓ | ✓ | ✓ | |
| PokéMortals | ✓ | ✓ | ✓ | N/A | |
| Pokémon Rejuvenation | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Rejuvenation Pt.2 - Where Love Lies | ✓ | ✓ | ✓ | N/A | ✓ |
| Pokémon Solar Eclipse | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Soulstones 2: Time Wardens | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Unbreakable Ties | ✓ | ✓ | ✓ | ✓ | |
| Pokémon Unchosen | ✓ | ✓ | ✓ | ✓ | N/A |
| Pokémon Z | ✓ | ✓ | ✓ | ✓ | ✓ |

### BattleDex

BattleDex replaces the live map during supported battles with the opposing Pokémon's Pokédex entry, sprite, types, weaknesses, measurements, and caught status. It follows opponent changes, supports switching between multiple active enemies, and returns to the map when battle ends. It is enabled by default and can be changed per game under **Live Map Details** in the map's tap menu. See the [BattleDex guide](DeveloperToolkit/14_BATTLEDEX.md) for integration details.

### Shiny starter choice

**Offer shiny starter** shows regular and shiny choices at supported starter selections and trades. Choose **Normal Odds** to keep the game's normal shiny roll or **Make Shiny** for the displayed Pokémon. It is enabled by default and can be disabled universally under **Map Settings**, **Starter Options**, or per game from the map's tap menu.

[Game Downloads](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/blob/main/GAME_DOWNLOADS.md)

## Install

1. Download the latest APK from [GitHub Releases](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases).
2. Open the APK and allow installation if Android asks.
3. Launch **Fanmake Pokémon Maps**.
4. Open **Maps** and select your game.
5. Choose the game's extracted main folder.

> Future updates will be prompted in the app when available.

The correct folder normally contains `Game.ini`, `Data`, and `Graphics`.

Versions shown on the Download page are the versions tested with the app. Nearby game versions may also work when their maps and core file structure have not changed.

## Using the app

- **Maps** contains every supported game, installed-game filters, collections, search, setup, downloads, guides, repair, and live-map launch controls.
- **Settings** separates hub behavior from universal map controls. Hub Settings includes library history, downloads, backup, restore, and recovery. Map Settings includes map display, markers, live details, and the overlay minimap.
- **About** contains the app version, updates, project links, privacy details, and controls.

Select a game to view its details. From there you can install or open it, repair its map setup, choose a different game folder, download the game, open its guide, or add it to collections. Removing an association does not delete the game or its files.

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

## What's new in 5.3

- Renamed **Small** to **Pixel Perfect** and corrected player, follower, character, door, rock, tree, and other live-map object sizing across all three icon-size options.
- Selected icon sizes now remain consistent while pinch zooming the map.
- Added a one-time **Map Welcome** screen explaining the Tap Menu, Pixel Perfect sizing, and visibility options. It can be shown again by running **Repair Map Setup**.
- Audited and corrected **170 setup screens and dialogs** across all 16 supported games, eliminating clipping, unintended scrolling, stretched artwork, and screen-boundary issues.
- Made **Instant** the default movement setting.
- Updated **Repair Map Setup** to restore the default map settings.

## What's new in 5.2

- Added BattleDex to all supported games.
- Added the BattleDex integration guide to the Developer Toolkit.

## What's new in 5.1

- New Accessibility Settings.
> Let me know if you have issues or would like a new accessibility feature.
- Fixed large doors on maps.
- Added map scale controls.
- Added Quick Launch and saved Maps hub choices.
- Improved map and player tracking.
- Added the Secrets page and collectible badges.

## What's new in 5.0

- Added PokéMortals, Pokémon Soulstones 2: Time Wardens, and Pokémon Unchosen. Unchosen includes separate English and French detection.
- Verified working live maps and all available sprite categories across all 16 supported games. (11 New Game Maps!)
- Rebuilt the Maps page with search, filters, collections, adjustable card widths, and layouts that adapt to different screens.
- Moved game setup, downloads, guides, repairs, folder changes, collections, and live-map launching into each game's details page.
- Updated the installer with automatic folder detection and Repair Setup that preserves working files.
- Split Settings into Hub Settings and Map Settings, with universal controls and per-game overrides.
- Added separate Player and Minimap marker choices, Current Player sprites where supported, and separate marker-size controls.
- Expanded live tracking for players, NPCs, Pokémon, followers, items, hidden items, collectibles, legendary encounters, missions, and quests.
- Added the shiny starter choice with regular and shiny previews where supported. It can be disabled globally or per game.
- Updated the overlay minimap with the OLED-style tap-menu design, automatic JoiPlay display selection, manual screen selection, an opacity slider, and more size presets.
- Lock Position now keeps the overlay box in place while pinch zoom changes the map inside it.
- Large sparse maps now open near the player, and Small marker size matches one map tile.
- Added Normal, 30 FPS, and Instant movement settings.
- Added centered loading animations during setup, repair, map loading, and other longer actions.
- Added a built-in Recently Played collection with a configurable one to five game history.
- Added resumable map-pack downloads with a setting to resume or restart interrupted downloads.
- Added settings export and restore without copying game folders, saves, map packs, or Android folder permissions.
- Maps pages now open immediately while missing game statuses are checked concurrently in the background, and returning from an unchanged ready map restores the existing page without another check.
- Added update download progress, cancellation, APK validation, and the private developer update code.
- Added About-page secrets and a new About background.
- Added developer documentation for trackers, installers, map packs, entities, quests, and basic test apps.
- Fixed a shared loading issue that could leave any game on Loading Map after its map image was generated.
- Fixed crashes when Android tried to draw a map image above its bitmap size limit.

## Request a game map

Use **Request Game Map** at the top of the Maps page, or submit a request through [GitHub Issues](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/issues).

## Privacy

Game folders and settings stay on your device. They are not uploaded. Internet access is used only for update checks and links that you choose to open.

## Disclaimer

Fanmake Pokémon Maps is an unofficial fan-made companion. It is not affiliated with or endorsed by Nintendo, Game Freak, Creatures Inc., The Pokémon Company, or the developers of the supported fan games. Pokémon and related trademarks belong to their respective owners.

## Developer toolkit

The Developer Toolkit documents the tracker format, game installation methods, map packs, entities, test applications, and BattleDex integration used by the application.

- [Developer Toolkit](DeveloperToolkit/README.md)
- [BattleDex integration guide](DeveloperToolkit/14_BATTLEDEX.md)

## Project cost and source

I considered charging $1 to $5 for version 5.0 and requiring a one-time payment check for future releases, with future updates included after payment. I decided to keep Fanmake Pokémon Maps free.

When the project is finished, I will release the complete source. The current [Developer Toolkit](DeveloperToolkit/README.md) documents the working integration system and will continue to be updated during development.

If you appreciate the work, please [give the project a star](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android) and consider [buying me a coffee](https://buymeacoffee.com/chimeragaming).
