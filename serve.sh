#!/usr/bin/env bash

set -euo pipefail

cd output && python -m http.server 8000
