"""Response envelope: docs/api-contract.md section 4, contracts/schemas/envelope.schema.json."""

from typing import Any

from pydantic import BaseModel, ConfigDict, Field

GENERIC_ERROR = "Something went wrong with that request. Please try again."
INVALID_REQUEST = "Sorry, I could not understand that request."


class Envelope(BaseModel):
    model_config = ConfigDict(extra="forbid")

    ok: bool
    fallback: bool
    message: str = Field(max_length=300)
    data: dict[str, Any] | None
    request_id: str = Field(min_length=1, max_length=64)


def success(data: BaseModel | dict[str, Any], request_id: str) -> Envelope:
    payload = data.model_dump() if isinstance(data, BaseModel) else data
    return Envelope(ok=True, fallback=False, message="", data=payload, request_id=request_id)


def fallback(message: str, request_id: str) -> Envelope:
    """Provider timed out, failed or returned nothing: the phone speaks `message`."""
    return Envelope(ok=True, fallback=True, message=message, data=None, request_id=request_id)


def rejected(message: str, request_id: str) -> Envelope:
    """Invalid, oversize, unauthorized or refused request."""
    return Envelope(ok=False, fallback=False, message=message, data=None, request_id=request_id)
