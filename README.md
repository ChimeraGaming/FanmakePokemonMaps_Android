> ⚠️ Back up your saves before installing a new map.

# Pixel Navigator
https://github.com/ChimeraGaming/PixelNavigator
- New Repo for ROMs doing the same thing as below (hopefully)

[![Release v0](https://img.shields.io/badge/release-v0-8957e5?style=for-the-badge&logo=github)](https://github.com/ChimeraGaming/PixelNavigator/releases)
![Android](https://img.shields.io/badge/Android-compatible-3DDC84?style=for-the-badge&logo=android&logoColor=white)
[![Star that repository](https://img.shields.io/github/stars/ChimeraGaming/PixelNavigator?style=for-the-badge&logo=github&label=STAR%20THIS%20REPOSITORY&color=f5c542)](https://github.com/ChimeraGaming/PixelNavigator)

> ⚠️ Back up your saves before installing a new map.

# Fanmake Pokémon Maps

[![Release v6.0](https://img.shields.io/badge/release-v6.0-8957e5?style=for-the-badge&logo=github)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android/releases)
![Lite Android](https://img.shields.io/badge/Lite%20Android-compatible-3DDC84?style=for-the-badge&logo=android&logoColor=white)
[![Star this repository](https://img.shields.io/github/stars/ChimeraGaming/FanmakePokemonMaps_Android?style=for-the-badge&logo=github&label=STAR%20THIS%20REPOSITORY&color=f5c542)](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android)

Fanmake Pokémon Maps is an Android live-map and battle companion for Pokémon fan games. It follows your position, switches maps as you move between areas, and can display the opposing Pokémon's Pokédex information during battles.

The interface supports dual-screen handhelds such as the AYN Thor and compatible Android phones, tablets, and handhelds.

Current release: **6.0** (build 59)

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

Select a game to view its details. From there you can install or open it, repair
its map setup, choose a different game folder, download the game, open its guide,
or add it to collections. Removing an association does not delete the game or
its files.

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

When the project is finished, I will release the complete source. The current [Developer Toolkit](DeveloperToolkit/README.md) documents the integration system and will continue to be updated during development.

Project support links: [Star this repository](https://github.com/ChimeraGaming/FanmakePokemonMaps_Android) and [Buy me a coffee](https://buymeacoffee.com/chimeragaming).
