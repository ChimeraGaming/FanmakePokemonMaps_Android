# Map Pack Format

## Image naming

Map images use the RPG Maker map ID:

```text
Map001.png
Map002.png
Map003.png
```

IDs with more than three digits are supported. PNG is preferred. WebP is also supported by the current installer.

## Archive layout

Place map images in a ZIP archive. They may be at the archive root or inside folders because the installer uses each entry's final filename.

Do not include duplicate IDs in PNG and WebP form. The installed map index prefers PNG when both exist.

## Validation

Record these values in the game configuration:

- HTTPS download URL
- Exact archive filename
- SHA-256 hash
- Expected unique map count

The installer should reject an unreadable image, a failed hash, or an archive whose map count does not match.

## Installed map folder

Each game uses a unique folder under its selected game root. Examples include:

```text
PokemonAnilMaps
PokemonPokemortalsMaps
PokemonUnchosenMaps
```

Do not use one shared map folder for multiple games.

## Keeping valid maps

During a normal update, compare image dimensions and keep valid installed maps. Replace only missing or damaged images.

During a full Repair, download and verify a clean archive, then replace the selected game's map images.

## Performance

Index the map directory once into map ID and file pairs. Do not call `findFile` across a large document provider folder every time the player changes maps.

Install large archives away from the main thread. A small fixed worker pool can write several maps in parallel without overwhelming Android storage.

