# BattleDex

BattleDex replaces the live map with information about the opposing Pokémon while a battle is active. It reads battle state from `ZMapTracker.txt` and loads Pokédex artwork and Pokémon sprites from the selected game folder.

BattleDex reports opponents in wild, trainer, scripted, Safari, double, and multi-battles. It does not report the player's Pokémon.

## Display behavior

When BattleDex is enabled:

1. `battle_active=1` opens BattleDex when at least one valid enemy record is present.
2. A single enemy is displayed without a switch button.
3. Multiple living enemies are displayed with a button that selects the next enemy.
4. A switch, send-out, transformation, summon, or faint updates the enemy list.
5. `battle_active=0` closes BattleDex and restores the live map.

The BattleDex option is stored in the selected game's map settings. It is enabled by default until the user changes it.

The Android reader keeps the last valid battle state if `battle_active` is temporarily absent during a split tracker write. The tracker must still write `battle_active=0` when the battle ends.

## Tracker fields

BattleDex adds two top-level tracker fields:

| Key | Type | Description |
| --- | --- | --- |
| `battle_active` | Integer | `1` while a battle is active, otherwise `0` |
| `battle_enemies` | Records | Living opposing Pokémon separated with `|` |

Example:

```text
battle_active=1
battle_enemies=26,Raichu,50,ELECTRIC/POISON,GROUND X4/PSYCHIC X2,It discharges electricity from its cheeks.,Mouse,8,300,Graphics/Pokemon/Front/RAICHU.png,RAICHU,1
```

Write an empty value when a battle has no displayable opponent:

```text
battle_enemies=
```

## Enemy record format

Each enemy record contains 12 comma-separated fields in this order:

| Position | Field | Type | Description |
| --- | --- | --- | --- |
| 1 | Species number | Integer | National or game Pokédex number; the parser rejects the record when this is not an integer |
| 2 | Name | Text | Display name from the game data |
| 3 | Level | Integer | Current battler level |
| 4 | Types | Text | Current type names separated with `/` |
| 5 | Weaknesses | Text | Super-effective type labels separated with `/` |
| 6 | Pokédex entry | Text | Entry text from the current species or form |
| 7 | Category | Text | Category without the word `Pokémon`, such as `Mouse` |
| 8 | Height | Integer | Decimeters |
| 9 | Weight | Integer | Hectograms |
| 10 | Sprite path | Text | Path inside the selected game root or the BattleDex export folder |
| 11 | Species key | Text | Stable species identifier such as `RAICHU` |
| 12 | Caught | Integer | `1` when the player owns the species or form, otherwise `0` |

Replace line breaks with spaces. Replace commas and pipe characters inside text fields with semicolons. Commas separate fields and pipes separate enemy records.

Use a numeric species number even when the engine identifies species with symbols. Essentials versions without `id_number` need a stable species ordinal or another game-specific number source.

## Collecting opponents

Read the battle's current battler collection and keep battlers that meet all of these conditions:

- The battler is on the opposing side.
- The battler has a Pokémon or enemy record.
- The battler is active and not fainted.
- The same Pokémon object has not already been added.

Deduplication is required for Safari implementations that expose the same temporary battler at more than one index. Do not merge different enemy Pokémon that share a species.

Refresh the records after the engine completes a send-out or replacement method. Publishing only at battle start leaves a stale opponent after a switch.

Common Ruby hook points include:

- `PokeBattle_Battle#pbStartBattle`
- `Battle#pbStartBattle`
- `pbOnActiveOne`
- `pbOnActiveAll`
- `pbOnBattlerEnteringBattle`
- `pbRecallAndReplace`
- `pbSendOut`

Method signatures differ between Essentials releases and forks. Accept `*args`, call the original method, preserve its return value, and publish the updated roster after the method completes.

Clear the battle state from an `ensure` block around the complete battle method when the engine does not provide one reliable end callback. Cover normal wins, losses, running, captures, and exceptions.

RPG Maker MV games need hooks for their battle manager and battle scene. A tactical battle plugin may keep enemies in its own entity collection instead of the normal troop collection. Inspect the active battle implementation before choosing the source.

## Types and weaknesses

Prefer the battler's current types. Use form-aware species data when the battler does not expose current types.

Calculate weaknesses with the game's own type chart. Keep attacks whose combined multiplier is greater than neutral. Do not report resistances or immunities.

Output labels in this form:

```text
GROUND X4/PSYCHIC X2
```

Do not assume one neutral-effectiveness constant across engine versions. Relevant implementations include:

- Floating-point multipliers in newer Essentials releases
- Integer multipliers in Essentials 19 and 20
- Legacy `PBTypes` charts with different neutral baselines between forks
- Rejuvenation type modifiers and inverse battles

Skip pseudo-types and engine-only special types.

## Pokédex metadata

Use the current form's data when it is available. Collect:

- Species number and key
- Name
- Category
- Pokédex entry
- Height
- Weight
- Caught state

Older games may store category and entry text in message tables. Some legacy games store height and weight in the binary Pokédex data file.

Keep the game's own language and custom entries. Set `battleDexUseEnglishFallback = true` only when the game has no usable Pokédex metadata. The offline fallback leaves unmatched custom species unchanged.

## Sprite paths

Return the game's resolved front-sprite path for ordinary PNG sprites. The Android app loads the file on a background thread and crops the first visible frame from horizontal animation strips and supported grid sheets.

Do not decode and rewrite an ordinary sprite during battle start. Synchronous bitmap export can pause the game on the first uncached battle.

Processed export is required when a source path does not identify the image shown in battle. Current examples include:

- Infinite Fusion sprites assembled from fusion parts, alternate sprites, and downloaded sprites
- Rejuvenation sprites selected from combined form, gender, shiny, front, and back sheets
- Encrypted RPG Maker MV battler images

Write processed files under:

```text
FanmakePokemonMapsBattleDex
```

The export filename must distinguish form, gender, shiny state, shadow state, and any game-specific alternate sprite selection. Reusing one filename for different variants can display the wrong opponent.

The Android path reader accepts normal game paths beginning with `Graphics/` and processed paths beginning with `FanmakePokemonMapsBattleDex/`.

## Native Pokédex artwork

Load Pokédex artwork from the user's selected game folder. Do not package the game's artwork in the app or developer toolkit.

Inspect the active Pokédex script before choosing assets. A game can contain unused Pokédex backgrounds, overlays, and page tabs from an older implementation.

`GeneratedGameConfig` provides these BattleDex fields:

| Field | Purpose |
| --- | --- |
| `battleDexBackgroundPaths` | Candidate base backgrounds; the first existing file is used |
| `battleDexOverlayPaths` | Candidate layer drawn over the base |
| `battleDexTabPaths` | Separate page-tab sheets |
| `battleDexTabArrowPaths` | Separate page-arrow sheet |
| `battleDexCombinedTabPaths` | Combined tab sheet used by some engines |
| `battleDexCombinedTabArrowPaths` | Combined arrow sheet |
| `battleDexCaughtIconPaths` | Candidate caught or owned icons |
| `battleDexDarkBodyText` | Uses dark text for category and measurements when `true` |
| `battleDexDarkLowerText` | Uses dark text for weaknesses and entry text when `true` |
| `battleDexDarkHeadingText` | Uses dark text for species number and name when `true` |
| `battleDexUseEnglishFallback` | Replaces known name, category, and entry fields with the offline English data |
| `battleDexSpriteCenterX` | Sprite center X in the 512 by 384 design space |
| `battleDexSpriteCenterY` | Sprite center Y in the 512 by 384 design space |
| `battleDexSpriteMaxSize` | Maximum sprite width or height in the design space |
| `battleDexHeightY` | Height row baseline |
| `battleDexWeightY` | Weight row baseline |
| `battleDexEntryStartY` | First Pokédex-entry text baseline |
| `battleDexEntryMaxLines` | Entry line limit; the view clamps this from 1 through 4 |

Set an asset list to `emptyList()` when matching files exist but the active game UI does not draw them.

Example configuration for a base and required overlay:

```kotlin
battleDexBackgroundPaths = listOf("Graphics/Pictures/Pokedex/bg_info.png"),
battleDexOverlayPaths = listOf("Graphics/Pictures/Pokedex/info_overlay.png"),
battleDexTabPaths = emptyList(),
battleDexTabArrowPaths = emptyList(),
battleDexDarkBodyText = true,
battleDexDarkLowerText = false,
battleDexDarkHeadingText = true,
battleDexSpriteCenterX = 100f,
battleDexSpriteCenterY = 97f,
battleDexSpriteMaxSize = 112f,
battleDexEntryStartY = 290f,
battleDexEntryMaxLines = 3,
```

Use empty background, overlay, tab, arrow, and caught-icon lists when the game has no Pokédex screen. `BattleDexView` then draws the app-owned fallback frame and caught icon.

## Adding BattleDex to a game

1. Confirm the engine's battle classes and loaded tracker location.
2. Identify the active opponent collection and battle lifecycle methods.
3. Add `battle_active` and `battle_enemies` to every complete tracker snapshot.
4. Collect all living opponents, not only the first battler.
5. Refresh records after switches, send-outs, transformations, and summons.
6. Clear the state for every battle exit path.
7. Resolve form-aware metadata, current types, weaknesses, caught state, and the displayed front sprite.
8. Inspect the game's active Pokédex implementation and configure only the layers it uses.
9. Render a 512 by 384 PNG preview and check text, sprite, type, weakness, and entry placement.
10. Increase the setup revision so Repair installs the changed tracker.

## Testing checklist

### Battle lifecycle

- Wild battle opens BattleDex.
- Trainer battle opens BattleDex.
- Scripted battle opens BattleDex.
- Safari battle reports one copy of the opponent.
- Double or multi-battle reports every living opponent.
- The switch button appears only when more than one opponent is present.
- Enemy switch updates the displayed species.
- Enemy transformation or summon updates the displayed species.
- Fainted opponents are removed.
- Win, loss, run, capture, and scripted exit restore the map.
- Turning BattleDex off keeps the map visible during battle.

### Data

- Species number and name match the opponent.
- Level matches the current battler.
- Form and types match the current battler.
- Weakness multipliers match the game's type chart.
- Inverse battle weaknesses are correct when the game supports inverse battles.
- Category, entry, height, and weight use the current form.
- Caught icon matches the player's Pokédex state.
- Custom species remain readable when no offline fallback record exists.

### Artwork

- Base, overlay, tabs, and arrows match the active game Pokédex.
- Heading, body, and lower text contrast with their panels.
- One-type and two-type opponents fit their type region.
- One through six weaknesses remain centered.
- Long names fit the heading.
- Entry text stays inside the lower panel.
- Static sprites, horizontal strips, grid sheets, forms, female sprites, shiny sprites, and alternate sprites show the expected frame.
- A missing caught icon uses the app-owned fallback icon.
- A game without Pokédex artwork uses the app-owned fallback frame.

### Performance and installation

- Folder and bitmap reads run away from the Android main thread.
- The first uncached battle does not pause while rewriting an ordinary PNG.
- Processed sprite exports are cached by the complete variant identity.
- Repeated battles reuse cached artwork.
- Repair installs the updated tracker after the setup revision changes.
- Debug Kotlin compilation succeeds.
- Signed release build succeeds.

## Troubleshooting

### BattleDex does not open

Confirm `battle_active=1`, then inspect `battle_enemies`. The parser drops a record when its species number is missing or not numeric.

Confirm BattleDex is enabled in the selected game's map settings.

### The map does not return after battle

Confirm the tracker writes `battle_active=0`. Add an end hook that covers running, captures, losses, scripted exits, and exceptions.

### The opponent stays after a switch

Hook the game's active send-out or replacement method. Publish after the original method updates the battler collection.

### Weaknesses are empty or incorrect

Check the engine's neutral multiplier, current battler types, pseudo-type filtering, and inverse-battle state. Do not apply an Essentials 21 multiplier rule to a legacy `PBTypes` chart.

### The sprite is missing

Confirm the reported path exists under `Graphics/` or `FanmakePokemonMapsBattleDex/`. Check form, gender, shiny, and alternate-sprite naming. RPG Maker MV encrypted images must be decrypted before Android can decode them.

### The wrong frame is displayed

Inspect the source image dimensions and frame layout. Export the exact processed bitmap when the file combines unrelated variants that the Android crop cannot identify.

### The first battle pauses

Return the existing sprite path for ordinary images. Restrict synchronous export to sprites that must be composed, decrypted, recolored, or cropped by the game runtime.

### Text is difficult to read

Set the heading, body, and lower text color flags independently. Render a PNG preview with the native background and overlay before building the APK.

## Source references

The production implementation is in these project files:

- `app/src/main/assets/generated/universal_entity_tracker.rb`
- `PokemonUnchosen/installer/pokemon_unchosen/FanmakePokemonMapsTracker.js`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedTrackerState.kt`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedGameConfig.kt`
- `app/src/main/java/com/chimeragaming/pokemonmaps/generated/GeneratedMapGameActivity.kt`
- `PokemonZMap/app/src/main/java/com/chimeragaming/pokemonzmap/BattleDexView.kt`
- `PokemonZMap/app/src/main/java/com/chimeragaming/pokemonzmap/BattleDexEnglish.kt`
