# Contracts — version 0.1.0-draft

The shared language between the Android app and the backend. Status: **DRAFT until Gate 1** (end of week 2). All six team members review, then it is frozen.

- `schemas/` — JSON Schema (draft 2020-12): `common` (enums), `event`, `envelope`, `api` (the 9 endpoints), `android` (on-phone seam types: SpeechRequest, ActionPlan, ScreenSnapshot).
- `fixtures/` — example documents for every response and failure. `fixtures/index.json` maps each fixture to its schema.
- Kotlin models (`android/contracts`) and Pydantic models (`backend/app/schemas`) are written to match these files, and both test suites parse every fixture.

Check everything: `python scripts/validate_contracts.py` (CI runs it on every PR).

## Change process after the freeze
1. Open a Technical Decision in Notion (and `docs/decisions/`).
2. Bump the version here and in `docs/api-contract.md`.
3. Update schemas, fixtures, Kotlin and Pydantic models in the same PR.
4. Reviewed by at least one Android and one backend owner.
