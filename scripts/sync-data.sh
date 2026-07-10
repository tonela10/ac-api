#!/usr/bin/env bash
# Copy seed JSON files into docs/data/ so they are served via GitHub Pages.
# Run this after editing anything in src/main/resources/data/.
set -euo pipefail

repo_root="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
src="$repo_root/src/main/resources/data"
dst="$repo_root/docs/data"

mkdir -p "$dst"
rsync -a --delete "$src/" "$dst/"

echo "Synced $src -> $dst"
git -C "$repo_root" status --short docs/data/ || true
