#!/usr/bin/env python3
"""Validate LOCAL Firebase Android config mapping without printing keys or project IDs.

Firebase Console's google-services.json contains project identifiers/API keys. This tool
checks app identity and environment isolation. It does NOT contact Firebase and it
cannot verify project ownership, API permissions, App Check or service reachability.
"""
import argparse
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
EXPECTED = {"dev": "net.thinkbloxph.siraba.dev", "staging": "net.thinkbloxph.siraba.staging", "prod": "net.thinkbloxph.siraba"}


def validate_one(path: Path, package: str) -> str:
    try:
        data = json.loads(path.read_text(encoding="utf-8"))
    except (OSError, json.JSONDecodeError) as exc:
        raise ValueError(f"Unreadable/invalid Firebase configuration: {path.name}") from exc
    info = data.get("project_info")
    clients = data.get("client")
    if not isinstance(info, dict) or not isinstance(clients, list):
        raise ValueError("Firebase configuration has no project_info/client list")
    project_id = info.get("project_id")
    project_number = str(info.get("project_number", ""))
    if not re.fullmatch(r"[0-9]+", project_number):
        raise ValueError("Missing/invalid Firebase project number")
    if not isinstance(project_id, str) or not project_id.strip():
        raise ValueError("Missing Firebase project ID")
    if "radiant" in project_id.casefold():
        raise ValueError("Reference-project identifiers may not be reused")
    matching = [c for c in clients if isinstance(c, dict) and
                c.get("client_info", {}).get("android_client_info", {}).get("package_name") == package]
    if not matching:
        raise ValueError(f"No Android client matches expected package {package}")
    # Ensure this is a real-format Firebase Android client export, rather than a
    # misleading partial hand-written stub. This is a format check, not a remote proof.
    for client in matching:
        app_id = client.get("client_info", {}).get("mobilesdk_app_id")
        api_keys = client.get("api_key", [])
        if (isinstance(app_id, str)
                and re.fullmatch(r"1:[0-9]+:android:[A-Za-z0-9]+", app_id)
                and isinstance(api_keys, list)
                and any(isinstance(key, dict) and isinstance(key.get("current_key"), str)
                        and key["current_key"].strip() for key in api_keys)):
            return project_id
    raise ValueError("Matching Android client lacks Firebase app ID or API key fields")


def validate_all(root: Path, require_dev: bool = False, require_all: bool = False) -> list[str]:
    errors = []
    seen_projects = {}
    for env, package in EXPECTED.items():
        path = root / "android-app" / "app" / "src" / env / "google-services.json"
        if not path.exists():
            if require_all or (require_dev and env == "dev"):
                errors.append(f"Missing {env.upper()} configuration at {path.relative_to(root)}")
            continue
        try:
            project = validate_one(path, package)
            if project in seen_projects:
                errors.append(f"{env.upper()} shares a Firebase project with {seen_projects[project]}; isolate environments")
            else:
                seen_projects[project] = env.upper()
        except ValueError as exc:
            errors.append(f"{env.upper()}: {exc}")
    return errors


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    group = parser.add_mutually_exclusive_group()
    group.add_argument("--require-dev", action="store_true", help="fail when DEV JSON is absent")
    group.add_argument("--require-all", action="store_true", help="fail unless DEV/STAGING/PROD configs exist")
    args = parser.parse_args()
    errors = validate_all(ROOT, args.require_dev, args.require_all)
    if errors:
        print("FAIL: Firebase configuration mapping")
        for msg in errors:
            print(f" - {msg}")
        return 1
    print("PASS (static): present Firebase JSON files have expected Android IDs and distinct projects.")
    if not args.require_dev and not args.require_all:
        print("NOTE: absent JSON files are allowed for unconfigured UI preview/CI; live Firebase NOT tested.")
    else:
        print("NOTE: file checks do NOT prove DEV Firebase reachability, Auth or App Check.")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
