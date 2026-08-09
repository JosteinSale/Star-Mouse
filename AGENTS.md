# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project overview

StarMouse is a 2D shooter/exploration game built with **LibGDX** (Java 17,
Maven). It's a one-man, work-in-progress hobby project by Jostein Sæle
(programming, art, animation, music, story all by the same person), so treat
it as a solo indie codebase, not a team-scale enterprise app — favor small,
direct changes over heavy abstraction.

The game alternates between several top-level modes, all driven from a single
`Game` object:

- **Exploring** — top-down walking-around segments (NPCs, dialogue, cutscenes)
- **Flying** — the shoot-'em-up space combat segments
- **Boss mode** — dedicated boss fight state
- **Cinematic** — scripted cutscenes
- Menu/meta states: `StartScreen`, `MainMenu`, `LevelSelect`, `LevelEditor`

## Build, run, test

- Requires **Java 17+** and **Maven**.
- Build: `mvn compile`
- Run tests: `mvn test`
- Entry point: `src/main/java/main_classes/MainClass.java` (creates a
  `Lwjgl3Application` wrapping `Game`). There's no `exec`/`assembly` Maven
  plugin configured, so run it via your IDE's "run main class" or
  `mvn exec:java -Dexec.mainClass=main_classes.MainClass` (add the exec plugin
  if not already present, or run through IntelliJ/VSCode).
- Packaging into an executable jar: see
  `documentation/how_to/export_game_to_executable.txt`.
- `main_classes/Testing.java` holds a `testingMode` flag — when true, save
  data is not written to disk (see `Game.saveDataToDisc()`).

## Repository layout

```
src/main/java/
  main_classes/     Game, MainClass, View, Testing — top-level wiring
  game_states/       One class per top-level mode (Exploring, Flying, BossMode,
                      Cinematic, MainMenu, LevelSelect, LevelEditor, ...)
  entities/           Game objects, split by mode: exploring/, flying/, boss_mode/
  entities/flying/enemies/   Enemy implementations + EnemyFactory/EnemyManager
  entities/flying/pickupItems/  Pickup items + factory
  projectiles/        Projectiles and shoot patterns
  cutscenes/          Cutscene sequencing: events/, effects/, cutscene_managers/
  rendering/          Draw logic, mirrors game_states/entities structure
                      (boss_mode/, exploring/, flying/, misc/, root_renders/)
  ui/                 HUD/menus/dialogue boxes
  data_storage/       Save/load (SaveData, ProgressValues, DataStorage)
  audio/              Music/SFX players
  inputs/             Keyboard input handling
  utils/              Constants, resource loading, parsing (CutsceneParser,
                      LevelDataParser, EventParser, DynamicValueParser)
src/main/resources/
  flying/, exploring/, boss_mode/, cinematic/   Per-mode assets: images,
      level_data/ (CSV), cutscenes/ (CSV)
src/test/java/       JUnit 5 + Mockito tests (currently utils/ parsers)
documentation/
  TODO.txt            Known bugs / planned work
  how_to/             Step-by-step guides for common content changes
```

## Architecture patterns to follow

- **Game state = its own class + its own renderer.** Each mode in
  `game_states/` has a matching renderer in `rendering/root_renders/`
  (e.g. `Flying` ↔ `RenderFlying`). Keep update/logic and draw code separated
  along this same line — don't put drawing calls inside `game_states`/`entities`
  classes.
- **Factories for extensible families.** New enemies go through
  `EnemyFactory`, new pickups through `PickupItemFactory`, new boss parts
  through `AnimatedComponentFactory`. When adding a new variant, register it
  in the relevant factory rather than special-casing call sites.
- **Level/cutscene data lives in CSV, not code.** `LevelDataParser`,
  `CutsceneParser`, and `EventParser` (in `utils/parsing/`) read the CSV files
  under `src/main/resources/*/level_data` and `*/cutscenes`. Prefer editing
  data files over hardcoding level content in Java.
- **Cutscenes are event-driven.** A cutscene is a `Sequence` of `GeneralEvent`
  subclasses in `cutscenes/events/`, each paired with an effect in
  `cutscenes/effects/` and dispatched via `cutscene_managers/`. New scripted
  behavior usually means adding one event class + one effect class, not
  branching existing ones.
- **Constants live in `utils/Constants.java`**, namespaced by mode (e.g.
  `Constants.Flying.TypeConstants`) — extend the existing nested structure
  rather than adding new top-level constant classes.

## Before adding new content

The `documentation/how_to/` folder has concrete step-by-step checklists —
follow them rather than reverse-engineering the pattern from scratch:

- `adding_new_enemies.txt`
- `adding_new_areas.txt`
- `adding_new_cutscenes.txt`
- `level_data_formatting_flying.txt` / `level_data_formatting_exploring.txt`
- `export_game_to_executable.txt`

Check `documentation/TODO.txt` before starting unrelated work — it lists known
bugs and in-progress design notes that may overlap with your task.

## Conventions

- Java 17, standard Maven layout, package-by-feature (not package-by-layer)
  under `src/main/java`.
- Indentation in existing files is 3 spaces — match the surrounding file
  rather than reformatting to a different style.
- Tests use JUnit 5 (`org.junit.jupiter`) + Mockito; current coverage is
  limited to parsers under `utils/`. Follow the existing test style in
  `src/test/java/utils/` for new tests.
- Binary/art assets (`.png`, `.ogg`, `.piskel`) are committed to the repo —
  don't attempt to regenerate or "clean up" these unless asked.
