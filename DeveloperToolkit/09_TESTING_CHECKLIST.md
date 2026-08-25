# Testing Checklist

## Folder and installer

- Correct root is accepted.
- One supported wrapper folder is accepted when configured.
- Wrong game folder is rejected.
- Read and write permission persists after app restart.
- Tracker installation is repeatable.
- Original archive or loader backup is created once.
- Repair refreshes tracker files.
- Save files remain unchanged.

## Live map

- Tracker format and game ID match.
- Map ID is greater than zero.
- Width and height are greater than zero.
- Player appears at the correct tile.
- All four movement directions are correct.
- Walking animation is correct.
- Map transition loads the matching image.
- A missing map shows a useful waiting or generation message.
- Closing and reopening both apps restores tracking.

## Sprites and markers

- Current player graphic matches the selected character.
- NPCs appear once.
- Pokémon appear once.
- Followers appear once.
- Invisible and erased events do not appear.
- Items do not include ordinary scenery.
- Hidden items are placed correctly.
- Quest markers follow the active event page.
- Completed quest markers disappear.

## Performance

- Folder validation runs away from the main thread.
- Only one tracker read runs at a time.
- Map directory is indexed once.
- Map bitmap changes only when map ID changes.
- Character graphics are cached.
- App switching does not create an application not responding message.

## Display

- Phone portrait fits without clipped controls.
- Phone landscape fits without stretched text.
- Tablet layouts stay inside the screen.
- Foldable top and bottom layouts remain usable.
- Fit mode preserves map aspect ratio.
- Overlay stays in its box while zooming.
- Locked overlay position still allows pinch zoom.

## Release

- Debug Kotlin compilation succeeds.
- Signed release build succeeds.
- APK version name and code are correct.
- APK signature is correct.
- Root release APK and staged APK have the same SHA-256.
- README, changelog, release notes, support table, and integration guide agree.

