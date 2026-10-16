from typing import Annotated

from fastapi import APIRouter, Depends, Request
from pydantic import BaseModel, ConfigDict

from app import __version__
from app.core.config import Settings, get_settings
from app.core.envelope import Envelope, success
from app.core.middleware import request_id_of

router = APIRouter(tags=["health"])


class HealthResponse(BaseModel):
    """Mirrors contracts/schemas/api.schema.json#/$defs/HealthResponse."""

    model_config = ConfigDict(extra="forbid")

    status: str
    version: str
    mock_mode: bool


@router.get("/health", response_model=Envelope)
async def health(
    request: Request, settings: Annotated[Settings, Depends(get_settings)]
) -> Envelope:
    data = HealthResponse(status="ok", version=__version__, mock_mode=settings.mock_mode)
    return success(data, request_id_of(request))
