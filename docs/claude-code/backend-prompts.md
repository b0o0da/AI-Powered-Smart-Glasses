# AVN backend — Claude Code prompts

The rules are already in `backend/CLAUDE.md`, which Claude Code reads automatically. Paste these prompts one at a time, in order. Review and merge each phase before starting the next. Use plan mode first (Shift+Tab) on every phase.

## Kickoff (no code)

```text
Read CLAUDE.md, backend/CLAUDE.md, docs/api-contract.md, contracts/ and the backend sections of docs/architecture.md (WS-15, WS-22, WS-23, "Backend capability breakdown").
Do not write code yet. In plan mode produce:
1. The package tree you will create under backend/.
2. The Pydantic models you will write, mapped one-to-one to contracts/schemas.
3. The provider interfaces (VlmProvider, MapsProvider, repositories) and their mock implementations.
4. The pyproject dependencies with exact versions.
5. Anything in the contract that is ambiguous or missing.
Stop and wait for approval.
```

## Phase B1 — Mock backend for all 9 endpoints (WS-22 · Mostafa S. · due Gate 1)

```text
Read backend/CLAUDE.md. Branch: feature/backend-mock.
Build the FastAPI app with all 9 endpoints answering from MockProvider/MockMaps using contracts/fixtures/api/ as the canned data:
- core/config with pydantic-settings reading .env; core/auth (X-API-Key, /health open); simple per-key rate limit; request_id middleware; JSON-lines logger that never logs payload content.
- Envelope helper; global exception handler that returns ok:false with a spoken-safe message.
- X-Mock-Scenario header: ok | timeout (sleep past the timeout, then fallback) | error | empty.
- Input caps: image base64 size, 768 px decode check, text length caps, extra fields forbidden.
- Dockerfile (python:3.12-slim, non-root) and docker-compose.yml with api + pgvector/pgvector:pg16, healthchecks.
Tests: every endpoint x every scenario, unauthorized, oversize image, invalid body, and a contract test that every fixture in contracts/fixtures/api parses.
Done when `docker compose up` serves all endpoints and `pytest` is green with no network. Report and stop.
```

## Phase B2 — Real AI providers and prompts (WS-15 · Mahmoud)

```text
Read backend/CLAUDE.md. Branch: feature/backend-providers.
- VlmProvider interface: complete(prompt, images, max_tokens, timeout) -> ProviderResult(text, usage, latency, provider).
- AnthropicProvider and GeminiProvider selected by VLM_PROVIDER; MockProvider stays default in tests.
- Prompt loader for nlp/prompt_templates/*.md (name, version, system, user slots). Write v1 templates: describe (1-2 short sentences, left/right relative, no safety claims, "I may be wrong" when unsure), ask, screen_summarize (bounded), intent (returns JSON steps from the whitelist only), ocr_fallback.
- Services for /describe, /ask, /screen/summarize, /intent using the provider; response sanitizer (length cap, strip markdown); small in-memory cache for repeated requests; cost/usage logging per call.
- /intent: strict JSON parse, whitelist validation, drop unknown steps, clarification when nothing valid, requires_confirmation for send/delete/buy/pay.
Tests: prompt builder unit tests, plan validator tests, sanitizer tests, provider timeout -> fallback. Live tests marked @pytest.mark.live.
Also add tests/fixtures/images/ golden-set README describing the rubric (hallucination, brevity, usefulness). Report and stop.
```

## Phase B3 — Database and faces (WS-23 · Mahmoud)

```text
Read backend/CLAUDE.md. Branch: feature/backend-db.
- SQLAlchemy 2 models + Alembic migrations: users, settings, known_people, face_embeddings (pgvector, dimension from config, model_version column), saved_places, request_history (text only), event_log.
- Repositories behind interfaces; /faces/enroll (consent required, embeddings only, image fields rejected), /faces/match (cosine search, conservative threshold, default unknown), delete-person service.
- Encrypt embeddings at rest (proposed: application-level encryption with a key from env).
Tests: migration up/down in a test container or skipped if Docker is absent, similarity query, delete cascade, consent missing, dimension mismatch. Report and stop.
```

## Phase B4 — Route proxy (WS-20 backend side · Mostafa S. with Mostafa O.)

```text
Read backend/CLAUDE.md. Branch: feature/backend-route.
- MapsProvider (Google Directions, walking) + MockMaps using contracts/fixtures/api/route.ok.json.
- Text destinations ("nearest pharmacy") resolved inside the provider with a places search near the origin.
- Cache key: origin rounded to ~50 m + destination; TTL; on provider failure return the cached route or a fallback message; daily quota guard.
- Never log coordinates.
Tests: cache hit/miss, provider failure with and without cache, quota exceeded. Report and stop.
```

## Phase B5 — Deploy (WS-22 · Mostafa S.)

```text
Read backend/CLAUDE.md. Branch: feature/backend-deploy.
- Production compose file for a Linux VPS with a reverse proxy providing HTTPS (Caddy proposed), restart policies, volume for Postgres, no ports except 443/80 exposed.
- docs/deploy.md: provisioning steps, env setup, spend limits to set in each provider console, how to rotate APP_API_KEYS, backup of the database.
- A /health uptime check (cron curl or a free monitor) and how alerts reach the team.
Do not run anything against a real server. Report and stop.
```
