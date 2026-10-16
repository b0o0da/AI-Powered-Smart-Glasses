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

| Folder | What it holds | Main owner |
| --- | --- | --- |
| `android/` | Kotlin + Compose app (`:app`, `:contracts`, `:engine`) | Mostafa S., Loay |
| `backend/` | FastAPI service, providers, Docker | Mostafa S., Mahmoud |
| `contracts/` | JSON schemas and fixtures shared by Android and backend | Mostafa S. |
| `computer_vision/` | Detector training, export (LiteRT/ONNX), evaluation | Mostafa O. |
| `nlp/prompt_templates/` | Versioned VLM/LLM prompts | Mahmoud |
| `datasets/` | Download scripts and labeling guidelines (data itself is NOT in Git) | Mahmoud |
| `models/` | `manifest.json` only (weights are fetched, never committed) | Mostafa O. |
| `tests/` | Shared fixtures, E2E tests, mock screens | Loay |
| `docs/` | Architecture, API contract, decisions, Claude Code prompts | Mahmoud (docs owner) |
| `scripts/` | Tooling: model fetch, contract validation, GitHub setup | Mostafa S. |
| `.github/` | CI, PR checks, PR template, issue forms, CODEOWNERS | Mostafa S. |

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
