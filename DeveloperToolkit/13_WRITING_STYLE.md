# Writing Style

Use plain, literal technical language throughout the project.

This rule applies to:

- UI text
- Documentation
- README files
- Code comments
- Commit descriptions
- Error messages
- Setup instructions

State what a feature reads, writes, displays, installs, or checks. Include limits and required conditions when they matter.

Do not use promotional adjectives, sales language, filler transitions, or vague quality claims. Do not describe ordinary behavior as special.

Keep exact names when they are required for a game item, move, API, file, screen orientation, library field, or other technical identifier.

## Examples

Use:

```text
Loads map images from the selected game folder.
```

Do not add claims about the quality of the loading process.

Use:

```text
Checks the selected folder for ZMapTracker.txt and the configured map folder.
```

Do not replace the file checks with a general compatibility claim.

Use:

```text
The tracker file could not be read. Select the game folder again.
```

Error messages should state the failure and the next action.

