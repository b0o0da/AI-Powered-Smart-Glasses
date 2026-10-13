# AVN Android app

Built phase by phase with Claude Code; the prompts are in `docs/claude-code/android-prompts.md` and the rules in `android/CLAUDE.md`. Phase 1 (skeleton) is in place: every feature seam is an interface bound to a fake.

## Modules
- `:contracts` — pure Kotlin models matching `contracts/schemas/`. Its tests read `contracts/fixtures/` in place.
- `:engine` — pure Kotlin. Holds `EngineInput` and `SpeechOutput`; the priority engine arrives in Phase 3.
- `:app` — Android, by feature package (`core`, `data`, `camera`, `vision`, `voice`, `screen`, `navigation`, `sos`, `ui`).

## Build
Requirements: JDK 17 and the Android SDK (Android Studio latest stable installs both). Run from `android/`:

```bash
./gradlew assembleDebug check
```

`check` runs the unit tests, Android lint and ktlint for all modules. No phone is needed. `./gradlew ktlintFormat` fixes style findings.

## Fakes and real implementations
`app/src/main/kotlin/avn/app/core/di/ModulesModule.kt` is the only place a seam is bound. The gradle property `avn.realSeams` (comma-separated seam names, empty by default) is exposed as `BuildConfig.REAL_SEAMS` so integration can switch one seam per build.

## On a phone
Install the debug APK and follow `docs/permissions-guide.md`. The Debug tab has the fake wake word, transcript and event injection, the offline toggle and the `X-Mock-Scenario` choice.
