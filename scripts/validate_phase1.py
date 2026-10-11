#!/usr/bin/env python3
"""SB-P1-001 portable validation runner (Windows, macOS, Linux).

Never deploys or writes to Firebase. Prints bounded status only; detailed subprocess
logs stay local in gitignored validation-results/. It cannot attest real cloud auth,
manual UI navigation, GitHub CI, or full Phase-1 acceptance.
"""
from __future__ import annotations

import argparse
import json
import os
from pathlib import Path
import shutil
import subprocess
import sys
import tempfile
from datetime import datetime, timezone

ROOT = Path(__file__).resolve().parents[1]
RESULTS = ROOT / "validation-results"


def run(name: str, command: list[str], *, cwd: Path = ROOT, timeout: int = 600) -> dict:
    """Run a local command and capture evidence without printing possibly sensitive logs."""
    try:
        result = subprocess.run(command, cwd=cwd, capture_output=True, text=True,
                                errors="replace", timeout=timeout, check=False)
        output = (result.stdout or "") + "\n" + (result.stderr or "")
        log_name = name + ".log"
        (RESULTS / log_name).write_text(output, encoding="utf-8")
        if result.returncode == 0:
            status, detail = "PASS", "Command exited successfully"
        elif "UnknownHostException" in output or "Name or service not known" in output:
            status, detail = "BLOCKED", "Dependency host not reachable in this environment"
        else:
            status, detail = "FAIL", f"Exit code {result.returncode}; inspect local log"
        return {"name": name, "status": status, "detail": detail, "log": log_name}
    except FileNotFoundError:
        return {"name": name, "status": "BLOCKED", "detail": "Executable not installed / not on PATH"}
    except subprocess.TimeoutExpired:
        return {"name": name, "status": "BLOCKED", "detail": "Timed out: inspect toolchain/network"}


def skip(name: str, reason: str, status: str = "UNVERIFIED") -> dict:
    return {"name": name, "status": status, "detail": reason}


def validate(*, require_dev: bool = False, with_emulators: bool = False,
             with_device: bool = False) -> list[dict]:
    RESULTS.mkdir(parents=True, exist_ok=True)
    results: list[dict] = []
    py = sys.executable
    results.append(run("source_guard", [py, "scripts/verify-repository.py"]))
    results.append(run("firebase_config_tests", [py, "-m", "unittest", "discover", "-s",
                                                  "tests/repository", "-p", "test_*.py"]))
    config_args = [py, "scripts/validate_firebase_config.py"]
    if require_dev:
        config_args.append("--require-dev")
    results.append(run("firebase_config_mapping", config_args))

    kotlin = shutil.which("kotlinc")
    java = shutil.which("java")
    if kotlin and java:
        src = ROOT / "android-app/app/src/main/java/net/thinkbloxph/siraba/domain"
        with tempfile.TemporaryDirectory(prefix="siraba-domain-") as folder:
            jar = Path(folder) / "domain.jar"
            compile_cmd = [kotlin, str(src / "model/RepairCategory.kt"),
                           str(src / "rules/RepairScopeRules.kt"),
                           str(src / "rules/JobAcceptanceRules.kt"),
                           str(ROOT / "tests/kotlin/DomainSmoke.kt"),
                           "-include-runtime", "-d", str(jar)]
            compiled = run("kotlin_smoke_compile", compile_cmd, timeout=150)
            results.append(compiled)
            if compiled["status"] == "PASS":
                results.append(run("kotlin_smoke_execute", [java, "-jar", str(jar)]))
            else:
                results.append(skip("kotlin_smoke_execute", "Compilation not successful", "BLOCKED"))
    else:
        results.append(skip("kotlin_smoke_compile", "Requires Java and kotlinc on PATH", "BLOCKED"))
        results.append(skip("kotlin_smoke_execute", "Requires Java and kotlinc on PATH", "BLOCKED"))

    npm = shutil.which("npm")
    if npm:
        results.append(run("backend_fee_policy", [npm, "--prefix", "backend/functions", "run", "test:policy"]))
        # TypeScript SDK build requires npm install. Pure policy tests above do not.
        if (ROOT / "backend/functions/node_modules").is_dir():
            results.append(run("backend_typescript_build", [npm, "--prefix", "backend/functions", "run", "build"]))
        else:
            results.append(skip("backend_typescript_build", "Run npm install in backend/functions first"))
    else:
        results.append(skip("backend_fee_policy", "Requires Node.js and npm", "BLOCKED"))
        results.append(skip("backend_typescript_build", "Requires Node.js and npm", "BLOCKED"))

    wrapper = str((ROOT / "android-app" / ("gradlew.bat" if os.name == "nt" else "gradlew")).resolve())
    results.append(run("android_gradle_build", [wrapper, ":app:testDevDebugUnitTest", ":app:assembleDevDebug", "--no-daemon"],
                       cwd=ROOT / "android-app", timeout=900))

    if with_device:
        results.append(run("android_instrumentation", [wrapper, ":app:connectedDevDebugAndroidTest", "--no-daemon"],
                           cwd=ROOT / "android-app", timeout=900))
    else:
        results.append(skip("android_instrumentation", "Use --with-device after starting an Android emulator"))

    if with_emulators:
        firebase = shutil.which("firebase")
        test_deps = (ROOT / "tests/firebase/node_modules").is_dir()
        if firebase and test_deps:
            results.append(run("firebase_emulator_rules", [firebase, "emulators:exec", "--project",
                              "demo-siraba", "--only", "auth,firestore,storage",
                              "npm --prefix tests/firebase test"], timeout=900))
        else:
            results.append(skip("firebase_emulator_rules",
                                "Install Firebase CLI and npm dependencies in tests/firebase first", "BLOCKED"))
    else:
        results.append(skip("firebase_emulator_rules", "Use --with-emulators after installing the Firebase CLI/test dependencies"))

    results += [
        skip("firebase_dev_live", "Needs new Firebase DEV project, Firebase Console configuration and live authenticated verification"),
        skip("manual_ui", "Needs actual Android device or emulator navigation evidence"),
        skip("github_ci", "Needs a connected GitHub repository and a successful Actions run"),
    ]
    return results


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--require-dev", action="store_true", help="Fail if DEV google-services.json is missing")
    parser.add_argument("--with-emulators", action="store_true", help="Run Firebase emulator denial tests")
    parser.add_argument("--with-device", action="store_true", help="Run Android instrumentation tests on connected device")
    args = parser.parse_args()
    results = validate(require_dev=args.require_dev, with_emulators=args.with_emulators,
                       with_device=args.with_device)
    for item in results:
        print(f"{item['name']:28} {item['status']:11} {item['detail']}")
    report = {
        "task": "SB-P1-001",
        "timestamp_utc": datetime.now(timezone.utc).isoformat(),
        "scope": "AUTOMATED LOCAL TESTS ONLY; NOT PHASE-1 ACCEPTANCE",
        "results": results,
    }
    (RESULTS / "validation.json").write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    print("\nEvidence kept locally: validation-results/validation.json and *.log (gitignored)")
    print("NOTE: Phase 1 acceptance additionally requires live Firebase DEV, device navigation and CI evidence.")
    return 1 if any(x["status"] == "FAIL" for x in results) else 2 if any(
        x["status"] in ("BLOCKED", "UNVERIFIED") for x in results) else 0


if __name__ == "__main__":
    sys.exit(main())
