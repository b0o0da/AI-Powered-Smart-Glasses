# AVN Android app

Not generated yet. The app is built phase by phase with Claude Code:

1. Open this repo in Claude Code (repo root or `android/`). `android/CLAUDE.md` holds the rules and is read automatically.
2. Paste the **kickoff prompt** from `docs/claude-code/android-prompts.md` (Part B). It plans without writing code.
3. After the plan is approved, run Phase 1 (skeleton, Mostafa S.) on `feature/android-skeleton`, then the other phases in order, each by its owner.

Planned modules: `:app` (Android, feature packages), `:contracts` (pure Kotlin models from `contracts/`), `:engine` (pure Kotlin priority engine).

Requirements: Android Studio latest stable, JDK 17+, an Android 12+ test phone (see the phone spec notes in Notion).
