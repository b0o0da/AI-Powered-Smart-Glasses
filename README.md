# AVN — AI Assistive Vision & Navigation System

[![CI](https://github.com/b0o0da/AI-Powered-Smart-Glasses/actions/workflows/ci.yml/badge.svg?branch=develop)](https://github.com/b0o0da/AI-Powered-Smart-Glasses/actions/workflows/ci.yml)

An Android voice assistant for blind and low-vision users. It sees through the phone camera, reads the phone screen, navigates by voice and sends an SOS, all hands-free.

Research prototype and assistive aid. Not a medical device, not a white-cane replacement, no traffic-safety guarantee.

- **Team:** Mostafa S., Mostafa O., Loay, Marwan, Boda, Mahmoud
- **Plan:** 14 weeks from Sun 11 Oct 2026, 4 gates targeted at weeks 2, 5, 9 and 12; weeks 13–14 absorb slippage. The plan, tasks, live gate forecast, decisions and evidence live in the Notion workspace "AVN Workspace". Code and contract docs live here.
- **Hardware:** phone only. Despite the repository name, there are no glasses or custom hardware in scope (a wearable is a possible post-MVP step).

## Gates

| Gate | When | What must work |
| --- | --- | --- |
| G1 | End of week 2 | Contracts frozen, camera to speech works |
| G2 | End of week 5 | Detect, warn, read text, wake word and STT |
| G3 | End of week 9 | Complete demo works on the phone; feature freeze |
| G4 | End of week 12 | Field tests, signed APK, `v1.0` |

## Repository layout

Owners follow the workstream table in `docs/architecture.md` §6 (primary first, then supporting).

| Folder | What it holds | Owners |
| --- | --- | --- |
| `android/` (app core) | Kotlin + Compose shell, services, permissions, DI, state (WS-03, WS-25) | Mostafa S., Loay |
| `android/app/.../camera/` | CameraX frame pipeline (WS-04) | Loay, Mostafa S. |
| `android/app/.../vision/` | Detection, tracking, near/far, OCR, faces, currency (WS-05–09) | Mostafa O., Marwan |
| `android/engine/` | Priority and context engine, the only thing allowed to speak (WS-16) | Marwan, Mostafa O. |
| `android/app/.../voice/` | Wake word, STT, TTS, intents and voice flow (WS-11–14) | Boda, Mahmoud, Loay |
| `android/app/.../screen/` (incl. `notifications/`) | AccessibilityService, screen capture, notification reading (WS-17–19) | Loay, Marwan |
| `android/app/.../navigation/` | GPS, routes, audio and haptic cues (WS-20) | Mostafa O., Boda |
| `android/app/.../sos/` | SOS and emergency flow (WS-21) | Boda, Mostafa O. |
| `android/contracts/` | Kotlin mirror of the shared contract (WS-02) | Mostafa S., Marwan |
| `backend/` | FastAPI service, providers, Docker (WS-22, WS-23) | Mostafa S., Mahmoud |
| `contracts/` | JSON schemas and fixtures shared by Android and backend (WS-02) | Mostafa S., Marwan, Mahmoud |
| `computer_vision/` | Detector training, export (LiteRT/ONNX), evaluation (WS-05, WS-24) | Mostafa O., Marwan |
| `nlp/prompt_templates/` | Versioned VLM/LLM prompts (WS-15) | Mahmoud, Boda |
| `datasets/` | Download scripts and labeling guidelines (data itself is NOT in Git) (WS-24) | Mahmoud, Mostafa O. |
| `models/` | `manifest.json` only (weights are fetched, never committed) | Mostafa O. |
| `tests/` | Shared fixtures, E2E tests, mock screens (WS-26) | Loay, Mahmoud |
| `docs/` | Architecture, API contract, decisions, Claude Code prompts (WS-29) | Mahmoud, Loay |
| `scripts/` | Tooling: model fetch, contract validation, GitHub setup (WS-01) | Mostafa S., Loay |
| `.github/` | CI, PR checks, PR template, issue forms, CODEOWNERS (WS-01) | Mostafa S., Loay |

Security, privacy and ethics (WS-28) is cross-cutting and owned by Marwan with Mahmoud.

## Branches and pull requests

- `main`: tagged at each gate. `develop`: integration branch, merged at least twice a week.
- Work on `feature/<area>-<name>` branches: `android`, `backend`, `cv`, `voice`, `engine`, `screen`, `data`, `test`.
- PR title starts with the Notion task ID: `AVN-123: Add FakeDetector`. CI checks the title and branch name.
- Every PR is reviewed by someone from **another role**, and CI must be green.
- Never commit secrets, model weights, datasets, face data or field-test recordings.

## Getting started

1. Install Android Studio (latest stable), JDK 17+, Python 3.12, Docker Desktop and Git.
2. Copy `.env.example` to `.env` and keep `MOCK_MODE=true` until you have keys.
3. Validate the shared contracts: `python scripts/validate_contracts.py` (needs `pip install jsonschema`).
4. Run the backend (mock mode, no keys needed):
   ```bash
   cd backend
   docker compose up --build          # API + PostgreSQL/pgvector, http://localhost:8000/docs
   # or without Docker:
   pip install -e ".[dev]" && pytest && uvicorn app.main:app --reload
   ```
5. Android: open `android/` in Android Studio and run `./gradlew assembleDebug check`. See `android/README.md` and `docs/permissions-guide.md`.
6. Build with Claude Code, phase by phase:
   - Android: `docs/claude-code/android-prompts.md` (rules already in `android/CLAUDE.md`)
   - Backend: `docs/claude-code/backend-prompts.md` (rules already in `backend/CLAUDE.md`)

## Key documents

- `docs/architecture.md` — full architecture blueprint (workstreams WS-01..WS-30, priority engine deep dive)
- `docs/api-contract.md` — API and event contract (**draft until Gate 1**)
- `docs/decisions/README.md` — open technical decisions (TD-xxx)
- `docs/demo-script.md`, `docs/screen-capabilities.md`, `docs/permissions-guide.md`
- `docs/legacy/` — the earlier 6-month roadmap and task split, kept for reference (superseded by the blueprint)

## One-time GitHub setup (repo admin)

```bash
./scripts/create_labels.sh             # issue and PR labels
./scripts/setup_branch_protection.sh   # develop as default branch, protect main and develop
```

Fill in the real GitHub handles in `.github/CODEOWNERS` afterwards.

## License

MIT, see `LICENSE`. Third-party models keep their own licenses (YOLO detectors are AGPL-3.0, see TD-001 in `docs/decisions/`).
