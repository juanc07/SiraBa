#!/usr/bin/env python3
"""SB-P1-003: minimally repair missing Firebase Crashlytics Gradle build ID.

Run from the root of an already migrated Sira Ba? repository:
    python ../apply_siraba_crashlytics_fix.py

No network or Firebase Console calls; no credentials read; idempotent.
"""
from __future__ import annotations

from datetime import datetime
from pathlib import Path
import argparse
import io
import os
import tempfile
import zipfile


def one_replace(contents: str, needle: str, replacement: str, target: str) -> str:
    count = contents.count(needle)
    if count != 1:
        raise ValueError(f'{target}: expected one safe insertion point, got {count}; no files were changed')
    return contents.replace(needle, replacement, 1)


def plan_changes(root: Path) -> tuple[dict[Path, str], bool]:
    version_file = root / 'android-app/gradle/libs.versions.toml'
    project_file = root / 'android-app/build.gradle.kts'
    app_file = root / 'android-app/app/build.gradle.kts'
    files = [version_file, project_file, app_file]
    for file in files:
        if not file.is_file():
            raise ValueError(f'Missing required project file: {file}')
    previous = {file: file.read_text(encoding='utf-8-sig') for file in files}
    versions = previous[version_file]
    project = previous[project_file]
    app = previous[app_file]

    # Only insert missing pieces. If the repository diverged unexpectedly, fail closed.
    if 'crashlyticsGradle = "3.0.8"' not in versions:
        if 'crashlyticsGradle = ' in versions:
            raise ValueError('Existing different Crashlytics plugin version; inspect manually before changing')
        versions = one_replace(versions, 'googleServices = "4.5.0"',
                               'googleServices = "4.5.0"\ncrashlyticsGradle = "3.0.8"', str(version_file))
    plugin_line = 'firebase-crashlytics = { id = "com.google.firebase.crashlytics", version.ref = "crashlyticsGradle" }'
    if plugin_line not in versions:
        if 'id = "com.google.firebase.crashlytics"' in versions:
            raise ValueError('Different Crashlytics plugin alias already present; inspect manually')
        versions = one_replace(versions,
                               'google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }',
                               'google-services = { id = "com.google.gms.google-services", version.ref = "googleServices" }\n' + plugin_line,
                               str(version_file))

    project_line = '    alias(libs.plugins.firebase.crashlytics) apply false'
    if project_line not in project:
        project = one_replace(project,
                              '    alias(libs.plugins.google.services) apply false',
                              '    alias(libs.plugins.google.services) apply false\n' + project_line, str(project_file))

    app_line = '    apply(plugin = "com.google.firebase.crashlytics")'
    if app_line not in app:
        # Mirror existing conditional Google-services application, so the plugin is
        # enabled when a flavor has Firebase config and applied *after* Google-services.
        app = one_replace(app,
                          '    apply(plugin = "com.google.gms.google-services")',
                          '    apply(plugin = "com.google.gms.google-services")\n' + app_line,
                          str(app_file))

    updated = {version_file: versions, project_file: project, app_file: app}
    changed = {file: text for file, text in updated.items() if text != previous[file]}
    if changed and any(x not in versions + project + app for x in (
        'crashlyticsGradle = "3.0.8"', plugin_line, project_line, app_line)):
        raise ValueError('Post-edit integrity check failed; no files were changed')
    return changed, all(x in versions + project + app for x in (
        'crashlyticsGradle = "3.0.8"', plugin_line, project_line, app_line))


def run(root: Path) -> int:
    root = root.resolve()
    changes, complete = plan_changes(root)
    if not complete:
        raise ValueError('Incomplete fix detected; no files changed')
    if not changes:
        print('ALREADY APPLIED: Crashlytics Gradle plugin is configured; nothing changed.')
        return 0

    # Backup ONLY the three Gradle source files. Never archive .git or local secrets.
    label = datetime.now().strftime('%Y%m%d-%H%M%S')
    backup = root.parent / f'sira-ba-before-crashlytics-fix-{label}.zip'
    with zipfile.ZipFile(backup, 'x', compression=zipfile.ZIP_DEFLATED) as archive:
        for path in changes:
            archive.write(path, arcname=path.relative_to(root).as_posix())

    for path, value in changes.items():
        fd, temp = tempfile.mkstemp(prefix='.siraba-edit-', dir=path.parent)
        try:
            with os.fdopen(fd, 'w', encoding='utf-8', newline='\n') as out:
                out.write(value)
            os.replace(temp, path)
        finally:
            if os.path.exists(temp):
                os.unlink(temp)

    docs = root / 'docs'
    if docs.is_dir():
        report = docs / 'SB-P1-003_CRASHLYTICS_STARTUP.md'
        if not report.exists():
            report.write_text("""# SB-P1-003 — Crashlytics Build ID Startup Fix

## User-device evidence
- Samsung SM-A556E, Android 16; DEV application installed successfully.
- On October 11, 2026 AndroidRuntime reported `IllegalStateException: The Crashlytics build ID is missing`, during `FirebaseInitProvider` eager initialization before `MainActivity` rendered.
- Root cause: Crashlytics runtime SDK dependency present, but Crashlytics Gradle plugin not applied.

## Focused implementation
- Add `com.google.firebase.crashlytics` version `3.0.8` to Gradle version catalog.
- Register the plugin in root `build.gradle.kts` and apply it after Google Services when a Firebase configuration exists.
- No changes to auth, security rules, payment policy, package identity, or original Radiant Circle references.
- No Crashlytics collection consent policy changed: startup disables collection in `SiraBaApplication.kt`.

## Actual verification status
- Static file patch: applied; **runtime fix UNVERIFIED** pending clean build/install/launch on a device.
- Device verify: `./gradlew :app:clean :app:assembleDevDebug :app:testDevDebugUnitTest :app:installDevDebug --no-daemon`.
- Launch package: `net.thinkbloxph.siraba.dev`.
- Re-capture AndroidRuntime log after reopening the application; verify Compose welcome and customer/worker entry screens.
""", encoding='utf-8')
    print(f'PATCHED: {len(changes)} Gradle files; Crashlytics Gradle plugin 3.0.8 added.')
    print(f'BACKUP: {backup}')
    print('NEXT: cd android-app && ./gradlew :app:clean :app:assembleDevDebug :app:testDevDebugUnitTest :app:installDevDebug --no-daemon')
    print('STATUS: build, install and device launch NOT VERIFIED by this script.')
    return 0


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--root', type=Path, default=Path.cwd(),
                        help='Sira Ba? repository root (default: current directory)')
    args = parser.parse_args()
    try:
        return run(args.root)
    except Exception as exc:
        print(f'ERROR: {exc}')
        return 1


if __name__ == '__main__':
    raise SystemExit(main())
