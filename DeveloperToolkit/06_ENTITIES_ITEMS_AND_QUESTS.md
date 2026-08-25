# Entities, Items, and Quests

## Entity categories

The 5.0 renderer recognizes these categories:

- `pokemon`
- `npc`
- `followers`
- `object`
- `hm`
- `effects`

Use the current active event page. Ignore erased, transparent, fully invisible, and graphic-free events.

## Player and event graphics

For RPG Maker XP, character graphics normally come from `Graphics/Characters`.

For RPG Maker MV, character graphics normally come from `www/img/characters`. MV character sheets can contain several characters, so include `player_character_index` and the optional entity character index when available.

Some MV images use encrypted `.rpgmvp` files. Decode them with the game's encryption key before loading the PNG payload.

## Followers

Follower implementations vary by Essentials version and game.

Check the collections actually used by the game. Common sources include:

- `$game_temp.followers`
- `$PokemonTemp.dependentEvents`
- Characters held only by the current spriteset

Avoid publishing the same follower twice when it appears in more than one source.

## Items and hidden items

Prefer event commands and known item APIs over graphic names alone. Common signals include item ball calls, hidden item calls, item ball graphics, sparkles, and release-specific item scripts.

Do not treat rocks, trees, decorative balls, or ordinary interaction objects as items.

## Quests

Quest markers must represent a current coordinate-bearing event page. Useful evidence includes:

- Quest API calls
- Mission switches
- Mission variables
- Active event page conditions
- Quest icon graphics
- Named Diary progression state

Completed or inactive pages must stop reporting their previous markers.

Avoid broad matching for words such as help, request, diary, or objective. Those words can appear in normal dialogue.

## Auditing RPG Maker XP data

Run the project quest audit utility against extracted map data:

```text
python tools/audit_quest_triggers.py GAME_FOLDER
```

Review map events, common events, switch names, variable names, event commands, and page conditions. Add a game-specific detector only after identifying reliable state.

## Rendering coordinates

For an entity at real tile coordinate `entityX` and `entityY`:

```text
screenX = mapLeft + ((entityX + 0.5) / mapWidth) * drawnMapWidth
screenY = mapTop + ((entityY + 0.5) / mapHeight) * drawnMapHeight
```

The half-tile offset places the marker at the center of the tile.

