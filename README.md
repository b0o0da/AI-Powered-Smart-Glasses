# AVN — AI Assistive Vision & Navigation System

An Android voice assistant for blind and low-vision users. It sees through the phone camera, reads the phone screen, navigates by voice and sends an SOS, all hands-free.

Research prototype and assistive aid. Not a medical device, not a white-cane replacement, no traffic-safety guarantee.

- **Team:** Mostafa S., Mostafa O., Loay, Marwan, Boda, Mahmoud
- **Plan:** 12 weeks, 4 gates. The plan, tasks, decisions and evidence live in the Notion workspace "AVN Workspace". Code and contract docs live here.

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
| `scripts/` | Tooling: model fetch, contract validation | Mostafa S. |

## Branches and pull requests

- `main`: tagged at each gate. `develop`: integration branch, merged at least twice a week.
- Work on `feature/<area>-<name>` branches: `android`, `backend`, `cv`, `voice`, `engine`, `screen`, `data`, `test`.
- PR title starts with the Notion task ID: `AVN-123: Add FakeDetector`.
- Every PR is reviewed by someone from **another role**, and CI must be green.
- Never commit secrets, model weights, datasets, face data or field-test recordings.

## Getting started

1. Install Android Studio (latest stable), JDK 17+, Python 3.12, Docker Desktop and Git.
2. Copy `.env.example` to `.env` and keep `MOCK_MODE=true` until you have keys.
3. Validate the shared contracts: `python scripts/validate_contracts.py` (needs `pip install jsonschema`).
4. Build with Claude Code, phase by phase:
   - Android: `docs/claude-code/android-prompts.md` (rules already in `android/CLAUDE.md`)
   - Backend: `docs/claude-code/backend-prompts.md` (rules already in `backend/CLAUDE.md`)

## Key documents

- `docs/architecture.md` — full architecture blueprint (workstreams WS-01..WS-30, priority engine deep dive)
- `docs/api-contract.md` — API and event contract (**draft until Gate 1**)
- `docs/decisions/README.md` — open technical decisions (TD-xxx)
- `docs/demo-script.md`, `docs/screen-capabilities.md`, `docs/permissions-guide.md`
- `docs/legacy/` — the earlier 6-month roadmap and task split, kept for reference (superseded by the blueprint)
