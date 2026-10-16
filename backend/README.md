# AVN backend

Thin FastAPI service between the Android app and paid cloud services. Rules: `backend/CLAUDE.md`. Contract: `docs/api-contract.md` and `contracts/`.

## Status

Foundation in place: app factory, settings from `.env`, response envelope, request ids, fail-soft error handlers (no stack traces), `GET /health`, tests, Dockerfile and `docker-compose.yml` (API + PostgreSQL 16/pgvector).

Still to build, phase by phase from `docs/claude-code/backend-prompts.md`: the other 8 endpoints, `X-API-Key` auth and rate limit, `X-Mock-Scenario`, input caps (B1, due Gate 1), real providers (B2), database and faces (B3), route proxy (B4), deploy (B5).

## Run

```bash
cp ../.env.example ../.env       # keep MOCK_MODE=true until you have keys
docker compose up --build        # http://localhost:8000/docs
```

Without Docker (Python 3.12+):

```bash
python -m venv .venv && source .venv/bin/activate
pip install -e ".[dev]"
ruff check . && pytest
uvicorn app.main:app --reload
```
