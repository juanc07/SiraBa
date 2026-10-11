#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
# Requires Java 21, Firebase CLI and installed tests/firebase dependencies.
firebase emulators:exec --project demo-siraba --only auth,firestore,storage \
  "npm --prefix tests/firebase test"
