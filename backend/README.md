# AVN backend

Not generated yet. Built phase by phase with Claude Code:

1. `backend/CLAUDE.md` holds the rules and is read automatically.
2. Paste the kickoff prompt from `docs/claude-code/backend-prompts.md`, then phases B1 to B5 in order.
3. Phase B1 (mock backend for all 9 endpoints) is due at Gate 1 so the Android team never waits.

Once B1 exists: copy `.env.example` (repo root) to `.env`, keep `MOCK_MODE=true`, run `docker compose up` in `backend/`, and open `http://localhost:8000/docs`.
