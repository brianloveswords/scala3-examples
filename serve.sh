#!/usr/bin/env bash

set -euo pipefail

cd docs && python -m http.server 8000
