#!/usr/bin/env bash

set -euo pipefail

cd output && caddy file-server
