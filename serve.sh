#!/usr/bin/env bash
# Serves dist/ on http://localhost:8000 (the page fetches Diamondrush.jar, so file:// will not work).
cd "$(dirname "$0")/dist" && exec python3 -m http.server "${1:-8000}" --bind 127.0.0.1
