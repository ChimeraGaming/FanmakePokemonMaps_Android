# Writing Style

This project will become open source after I'm done with it. If you contribute code, documentation, UI text, or other written content, follow this style.

Use plain, literal technical language.

This applies to:

* UI text
* Documentation
* README files
* Code comments
* Commit descriptions
* Error messages
* Setup instructions

Describe what the code or feature actually does. State what it reads, writes, displays, installs, checks, changes, or requires.

Include limits, dependencies, required files, and other conditions when they matter.

Avoid:

* Promotional language
* Sales language
* Unnecessary adjectives
* Filler transitions
* Vague quality claims
* Describing normal functionality as special or impressive

Do not replace specific behavior with broad claims.

Keep exact names when referring to game items, moves, APIs, files, directories, screen orientations, library fields, classes, methods, settings, or other technical identifiers.

Prefer short sentences when they communicate the same information clearly.

## Examples

Use:

```text
Loads map images from the selected game folder.
```

Avoid adding claims about speed, reliability, ease of use, or quality unless they are measurable and relevant.

Use:

```text
Checks the selected folder for ZMapTracker.txt and the configured map folder.
```

Do not replace specific checks with a general statement such as:

```text
Checks whether the game is compatible.
```

Use:

```text
The tracker file could not be read. Select the game folder again.
```

Error messages should state what failed and, when possible, what the user can do next.

Use:

```text
Supports portrait and landscape layouts.
```

Do not write:

```text
Provides a flexible and seamless experience across multiple screen orientations.
```

When documenting code, explain behavior that is not obvious from the code itself. Do not add comments that only restate the code.
