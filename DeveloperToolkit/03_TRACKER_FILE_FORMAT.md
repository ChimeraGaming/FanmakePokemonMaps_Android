# Tracker File Format

`ZMapTracker.txt` is a UTF-8 key and value file. Each line contains one key, one equals sign, and its value.

## Required fields

| Key | Type | Description |
| --- | --- | --- |
| `tracker_format` | Integer | Must be `1` for the current generated map parser |
| `game_id` | Text | Must match the Android game configuration |
| `tracker_version` | Integer | Game-side tracker revision |
| `map_id` | Integer | Current RPG Maker map ID |
| `map_name` | Text | Current map display name |
| `x` | Integer | Player tile X coordinate |
| `y` | Integer | Player tile Y coordinate |
| `direction` | Integer | RPG Maker direction value |
| `map_width` | Integer | Map width in tiles |
| `map_height` | Integer | Map height in tiles |
| `updated_at` | Number | Tracker timestamp or update counter |

The parser rejects a tracker when `tracker_format` or `game_id` does not match. It also rejects a live state with a missing map ID, coordinate, width, or height.

## Smooth player fields

| Key | Type | Description |
| --- | --- | --- |
| `player_real_x` | Integer | X coordinate multiplied by 1000 |
| `player_real_y` | Integer | Y coordinate multiplied by 1000 |
| `player_pattern` | Integer | Current walking animation frame |
| `player_graphic` | Text | Character graphic name without the file extension |
| `player_character_index` | Integer | Optional RPG Maker MV character sheet index |
| `player_moving` | Integer | `1` while moving, otherwise `0` |

When smooth coordinates are absent, the Android parser uses `x` and `y`.

## Item fields

```text
map_items_unclaimed=2
map_item_positions=pokeball,12,8|hidden,20,4
```

Each item record contains type, X, and Y. The older two-value format is treated as a hidden item.

## Legendary fields

```text
map_legendary_positions=MEW,14,9,001_Mew
```

Each record contains species, X, Y, and graphic name.

## Quest fields

```text
map_quest_positions=7,5|18,11
```

Each quest record contains X and Y.

## Entity fields

```text
map_entities=12,npc,7500,9000,2,1,255,1,TrainerA,0
```

Each record contains:

1. Event or entity ID
2. Category
3. Real X multiplied by 1000
4. Real Y multiplied by 1000
5. Direction
6. Animation pattern
7. Opacity from 0 to 255
8. Moving flag
9. Graphic name
10. Optional RPG Maker MV character sheet index

Records are separated with `|`. Fields inside a record are separated with commas. Tracker text values must remove line breaks, commas, and pipe characters before writing.

## Map export fields

Runtime map generation can report:

```text
export_map_id=42
export_status=error
export_error=Map export failed
```

The Android app uses these fields to distinguish an export failure from a map that is still being created.

## Safe writing

Write one complete snapshot at a time. When possible, overwrite the existing file entry instead of deleting and recreating it. Some Android document providers keep a handle to the original entry.

If a runtime requires atomic replacement, the Android reader must recover by finding the new file and retrying once.

See [ZMapTracker.example.txt](examples/ZMapTracker.example.txt) for a complete sample.

