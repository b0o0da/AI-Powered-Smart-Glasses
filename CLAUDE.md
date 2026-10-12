# AVN monorepo — rules for Claude Code

This repo holds the AVN assistive app: an Android voice assistant for blind and low-vision users plus a thin FastAPI backend. Read the folder-specific file before working in a folder:

- `android/CLAUDE.md` — Android app rules (the priority engine is the only component allowed to speak)
- `backend/CLAUDE.md` — backend rules (9 endpoints, envelope, mock mode, no content logging)

## Sources of truth
- `docs/architecture.md`: the architecture blueprint. Workstreams are WS-01..WS-30; cite them in summaries.
- `docs/api-contract.md` and `contracts/`: the shared contract. Draft until Gate 1 (end of week 2), frozen after. After the freeze, never change a field, type or enum value unless told a Technical Decision exists (`docs/decisions/`).
- Run `python scripts/validate_contracts.py` after any change in `contracts/`.

## Global rules
- Interface and fake first, real implementation second. Fixtures are deliverables and live in `contracts/fixtures/` and `tests/fixtures/`.
- Fail soft: every failure yields a bounded result or a spoken fallback sentence, never a crash or a silent hang.
- Safety: hazards are `near | medium | far`, never meters, speed or time-to-collision. AI (VLM/LLM) output is informational only and never raises a safety alert.
- Privacy: no frames, screenshots, screen text, notification text, audio or face images are stored or logged. Faces are opt-in embeddings only.
- Never commit secrets, model weights, datasets or field recordings (see `.gitignore`).
- Branches: `feature/<area>-<name>` off `develop`. Commit messages and PR titles start with the Notion task ID (`AVN-123: ...`). Do not push or open PRs unless asked.
- Report real test results. Never claim something works without running it.
