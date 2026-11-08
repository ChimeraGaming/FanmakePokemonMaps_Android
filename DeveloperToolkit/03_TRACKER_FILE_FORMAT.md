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

## Encounter table

The main app's shared encounter panel reads the following fields on every supported map screen:

```text
encounter_mode=Land
encounter_species=Paras,Rattata
encounter_details=Land^Paras^46^10^31^2^4^1^PARAS^Graphics/Pokemon/Front/PARAS.png;Land^Rattata^19^10^31^2^4^0^RATTATA^Graphics/Pokemon/Front/RATTATA.png
encounter_context=Rain^Night^
owned_species=46
species_max=1025
```

Each detail record contains source, display name, species ID, weight, total weight for that source, minimum level, maximum level, caught flag, species key, and relative sprite path. Separate records with semicolons and fields with `^`. Remove those separators and line breaks from display text. Keep each source's total separate so fishing and land rates are not combined.

Use a species ID of `0` when the engine uses symbolic IDs, and supply the species key and sprite path. The Pokédex link resolves game species keys before numeric IDs. Missing levels, rates, and caught flags stay blank; do not invent values. Set both encounter fields to empty when an area has no encounter data so previous-area rows clear.

The main app draws a compact Possible Catches panel with sprites, two-line rows, and caught indicators. Drag its header, rows, or empty body space to reposition it. Tap the header to collapse it, or tap a row to open details. Pinch or use the corner handle to scale it uniformly. Long lists have a scrollbar at the right edge. Layout preferences use a separate `<game-preferences>_encounter_table` store with proportional position, scale, and collapsed state. They do not modify game or save data. New games using `GeneratedMapGameActivity` and `GeneratedLiveMapView` inherit these controls. Custom map hosts must publish complete snapshots with `EncounterTable.publish(activity, text)`, draw with `EncounterTable.draw(view, canvas)`, and give `EncounterTable.onTouch(view, event)` the first chance to handle touches. Keep ordinary map gestures for touches outside the panel.

Finish each snapshot with a newline. Partial snapshots are ignored. A same-map base-tracker handoff that omits encounter keys preserves the last complete table, preventing flicker while the shared extension publishes its records.

## Safe writing

Write one complete snapshot at a time. When possible, overwrite the existing file entry instead of deleting and recreating it. Some Android document providers keep a handle to the original entry.

If a runtime requires atomic replacement, the Android reader must recover by finding the new file and retrying once.

See [ZMapTracker.example.txt](examples/ZMapTracker.example.txt) for a complete sample.

