#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
TMP="$(mktemp -d)"
trap 'rm -rf "$TMP"' EXIT
SRC=android-app/app/src/main/java/net/thinkbloxph/siraba/domain
kotlinc "$SRC/model/RepairCategory.kt" "$SRC/rules/RepairScopeRules.kt" "$SRC/rules/JobAcceptanceRules.kt" tests/kotlin/DomainSmoke.kt -include-runtime -d "$TMP/domain-test.jar"
java -jar "$TMP/domain-test.jar"
