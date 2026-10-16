"""AVN backend app factory.

Foundation only: envelope, request ids, fail-soft error handlers and GET /health.
The other 8 endpoints, auth, mock scenarios and input caps arrive in phase B1
(docs/claude-code/backend-prompts.md).
"""

import logging

from fastapi import FastAPI
from fastapi.exceptions import RequestValidationError
from starlette.exceptions import HTTPException as StarletteHTTPException

from app import __version__
from app.core.config import get_settings
from app.core.middleware import (
    http_error_handler,
    request_context,
    unhandled_error_handler,
    validation_error_handler,
)
from app.routers import health


def create_app() -> FastAPI:
    settings = get_settings()
    logging.basicConfig(level=settings.log_level.upper(), format="%(message)s")

    app = FastAPI(title="AVN backend", version=__version__)
    app.middleware("http")(request_context)
    app.add_exception_handler(RequestValidationError, validation_error_handler)
    app.add_exception_handler(StarletteHTTPException, http_error_handler)
    app.add_exception_handler(Exception, unhandled_error_handler)
    app.include_router(health.router)
    return app


app = create_app()
