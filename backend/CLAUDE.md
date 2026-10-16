# AVN backend — rules for Claude Code

## What this service is
One thin FastAPI service between the AVN Android app and paid outside services (cloud VLM/LLM, Maps, database). It hides API keys, bounds every response, caches, and can run fully mocked. It answers questions; it is never on the safety path (hazard detection, SOS and alerts run on the phone).

## Source of truth
- `docs/api-contract.md` and `contracts/schemas/` + `contracts/fixtures/`. Pydantic models must match them exactly. Draft until Gate 1, frozen after: never change a field without being told a Technical Decision exists.
- `docs/architecture.md`: WS-15 (prompts and providers), WS-22 (backend core), WS-23 (database), "Backend capability breakdown".

## Stack (do not swap without asking)
Python 3.12, FastAPI + Uvicorn, Pydantic v2 + pydantic-settings, httpx (outbound calls and tests), pytest + pytest-asyncio, SQLAlchemy 2 + Alembic (proposed), PostgreSQL 16 + pgvector, Docker + docker compose, ruff for lint and format. Dependencies in `pyproject.toml`; pin versions.

## Layout
```
backend/
  app/
    main.py            # app factory, routers, middleware
    core/              # config (env), auth (X-API-Key), timeouts, logging, mock scenario, errors
    schemas/           # Pydantic models mirroring contracts/
    routers/           # one per endpoint group: health, describe, ask, screen, intent, faces, route, sos
    services/          # business rules: prompt building, caching, history, validation of plans
    providers/         # VlmProvider (anthropic, gemini, mock), MapsProvider (google, mock), repositories
    db/                # SQLAlchemy models, session, Alembic migrations
  tests/
  Dockerfile
  docker-compose.yml   # api + postgres/pgvector
  pyproject.toml
```
Prompt templates live in `nlp/prompt_templates/` (repo root), versioned files: describe, ask, screen_summarize, intent, ocr_fallback, translate.

## Rules
1. **Only the 9 endpoints** in the contract: GET /health, POST /describe, POST /ask, POST /screen/summarize, POST /intent, POST /faces/enroll, POST /faces/match, GET /route, POST /sos. Nothing else.
2. **Layering:** router (validate, authenticate) -> service (rules, context, cache, history) -> provider (behind an interface). Routers never call providers directly.
3. **Envelope on every response:** `{ok, fallback, message, data, request_id}`. Invalid input -> `ok:false` with a short spoken-safe message, never a stack trace.
4. **Timeouts:** every provider call has a server timeout (`PROVIDER_TIMEOUT_S`). Timeout, error or empty output -> `ok:true, fallback:true, message:"<speakable sentence>"`.
5. **Mock mode:** `MOCK_MODE=true` routes every provider to deterministic mocks. Header `X-Mock-Scenario: ok|timeout|error|empty` injects failures. The test suite must pass with no network and no keys.
6. **Auth:** `X-API-Key` checked against `APP_API_KEYS` on every endpoint except `/health`; rate limit per key.
7. **Input caps:** images are base64 JPEG, max 768 px longest side and a byte cap; text fields have length caps; unknown fields rejected.
8. **Logging:** JSON lines with request_id, endpoint, status, latency_ms, provider, token usage. NEVER log images, screen text, questions, answers, embeddings or coordinates.
9. **AI safety:** VLM/LLM answers are bounded in length, use "I may be wrong" wording when uncertain, and contain no safety claims. `/intent` validates every returned step against the whitelist (launch_app, wait, read_screen, summarize, scroll, back, tap_label, navigate, sos, describe, ask) and drops unknown steps; send/delete/buy/pay set `requires_confirmation`.
10. **Screens:** `/screen/summarize` re-checks the package deny-list (banking, wallets) and refuses blocked screens.
11. **Faces:** embeddings only, `consent:true` required, any image field rejected, stored with model version, hard delete supported.
12. **Keys** only from environment; never in code, tests, logs or Git.

## Testing
pytest + httpx `AsyncClient` per endpoint: valid, invalid, oversize, timeout, error, empty, unauthorized. Contract test: every fixture in `contracts/fixtures/api/` parses into the matching Pydantic model. Real-provider tests are marked `@pytest.mark.live` and skipped by default.

Before saying a task is done, run `ruff check . && pytest` and report the real output.

## Git
Branch `feature/backend-<name>` off `develop`. Commit messages start with the Notion task ID if given. Do not push or open PRs unless asked.
