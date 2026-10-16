import json
from pathlib import Path

import httpx
import pytest

from app.core.envelope import Envelope
from app.main import create_app
from app.routers.health import HealthResponse

FIXTURES = Path(__file__).resolve().parents[2] / "contracts" / "fixtures" / "api"


@pytest.fixture
async def client():
    transport = httpx.ASGITransport(app=create_app())
    async with httpx.AsyncClient(transport=transport, base_url="http://test") as c:
        yield c


async def test_health_ok_without_api_key(client):
    r = await client.get("/health")
    assert r.status_code == 200
    body = Envelope.model_validate(r.json())
    assert body.ok and not body.fallback and body.message == ""
    health = HealthResponse.model_validate(body.data)
    assert health.status == "ok"
    assert r.headers["X-Request-Id"] == body.request_id


async def test_request_id_is_echoed_when_safe(client):
    r = await client.get("/health", headers={"X-Request-Id": "req_abc_1"})
    assert r.json()["request_id"] == "req_abc_1"


async def test_unsafe_request_id_is_replaced(client):
    r = await client.get("/health", headers={"X-Request-Id": "bad id <script>"})
    assert r.json()["request_id"].startswith("req_")
    assert "script" not in r.json()["request_id"]


async def test_unknown_route_returns_envelope_not_stack_trace(client):
    r = await client.get("/does-not-exist")
    assert r.status_code == 404
    body = Envelope.model_validate(r.json())
    assert body.ok is False and body.data is None and body.message


def test_health_fixture_matches_models():
    raw = json.loads((FIXTURES / "health.ok.json").read_text())
    body = Envelope.model_validate(raw)
    HealthResponse.model_validate(body.data)


def test_error_fixture_matches_envelope():
    raw = json.loads((FIXTURES / "error.invalid_request.json").read_text())
    body = Envelope.model_validate(raw)
    assert body.ok is False and body.data is None
