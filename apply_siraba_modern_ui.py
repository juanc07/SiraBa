#!/usr/bin/env python3
"""SB-P1-004 Sira Ba? modern Compose onboarding UI patch.

Usage from the Sira Ba? repository root:
    python ../apply_siraba_modern_ui.py
or, when the script is in the repository root:
    python apply_siraba_modern_ui.py

Keeps Firebase JSON, backend, security rules, app identity and payments untouched.
Creates a file-only backup outside the source repository, with no .git or secrets.
"""
from __future__ import annotations
import argparse
import base64
import hashlib
import json
import os
from pathlib import Path
import tempfile
import zipfile
import zlib
from datetime import datetime

_BASELINE_HASHES = {'android-app/app/src/main/java/net/thinkbloxph/siraba/ui/theme/Theme.kt': 'ad78cd8d0d92d9c2f3043669c9f9f2a014a9a89b84e4223b9baff52b676ff473', 'android-app/app/src/main/java/net/thinkbloxph/siraba/ui/screens/WelcomeScreen.kt': 'fa56989c901f55777eab6a3a32dc08839219f932349c63cf194c9cb360de5f0c', 'android-app/app/src/main/java/net/thinkbloxph/siraba/ui/screens/RoleEntryScreen.kt': '00f42bf0b6b480dd70b46bc3ce0c5f06ad4c4e196205b47b43a6c91dc5ff6ce3', 'android-app/app/src/main/java/net/thinkbloxph/siraba/ui/navigation/SiraBaNavHost.kt': '99911126bf705e00a6cffa7bf96851f83c27fc8bccab77086bd55595c96b7fc2', 'android-app/app/src/main/java/net/thinkbloxph/siraba/ui/testing/UiTestTags.kt': '919f200d928c65ac47076e2567ea2795846b1f0499b98c45a87437da2d5bb276', 'android-app/app/src/androidTest/java/net/thinkbloxph/siraba/FoundationNavigationTest.kt': '048414a680136d9292de0d73855b982c4af47cafbdef2e8cc3e1a7fa73d440cd'}
_PAYLOAD = ("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")

_DECISION = "| ADR-016 | 2026-10-11 | Modern Material 3 Sira Ba? brand and onboarding; no new backend features | Previous screens were Phase 1 placeholders; preserve test tags and preview honesty | IMPLEMENTED SOURCE; device validation pending |"
_PHASE = "## SB-P1-004 — Modern onboarding UI (2026-10-11)\nMaterial 3 brand/theme, redesigned welcome/role previews, back navigation and stable test tags. The developer reports that the previous DEV build opens on Samsung; this UI change is NOT YET built or device-tested. Phase 1 remains IN PROGRESS, Phase 2 NOT STARTED. See `docs/UI_DESIGN_SYSTEM.md`."
_TEST = "## SB-P1-004 — Modern UI evidence (2026-10-11)\nSource patch completed. Static repository/Kotlin domain/backend policy checks passed on authoring machine; Android compilation, on-device accessibility and visual QA remain UNVERIFIED. Run `:app:testDevDebugUnitTest :app:assembleDevDebug :app:installDevDebug`, inspect both role routes and back navigation on phone, and record evidence."
_NEXT = "## SB-P1-004 — UI validation next\nRebuild DEV APK and navigate Welcome → Customer → Back → Worker → Back on Android. Check small screen, font scale, dark mode, TalkBack and readable preview disclosure; run instrumentation where possible. Phase 1 acceptance remains open."
_APPEND_DOCS = {
    'docs/DECISION_LOG.md': (_DECISION, '| ADR-016 |'),
    'docs/PHASE_STATUS.md': (_PHASE, '## SB-P1-004'),
    'docs/TEST_REPORT.md': (_TEST, '## SB-P1-004'),
    'docs/NEXT_STEPS.md': (_NEXT, '## SB-P1-004'),
}

def digest(s: str) -> str:
    return hashlib.sha256(s.encode('utf-8')).hexdigest()

def run(root: Path) -> int:
    root = root.resolve()
    required = root / 'android-app/app/build.gradle.kts'
    if not required.is_file():
        raise ValueError(f'Not a Sira Ba? repository root: {root}')
    gradle = required.read_text(encoding='utf-8')
    if 'net.thinkbloxph.siraba' not in gradle:
        raise ValueError('Expected ThinkBlox PH package migration first; no files changed')
    files = json.loads(zlib.decompress(base64.b64decode(_PAYLOAD)).decode('utf-8'))
    changes: dict[Path, str] = {}
    for rel, intended in files.items():
        path = root / rel
        if not path.resolve().is_relative_to(root):
            raise ValueError('Unsafe destination path')
        if path.is_file():
            old = path.read_text(encoding='utf-8')
            if old == intended:
                continue
            allowed = _BASELINE_HASHES.get(rel)
            if allowed is None or digest(old) != allowed:
                raise ValueError(f'File has local changes that must be reviewed before patching: {rel}')
        changes[path] = intended
    for rel, (text, marker) in _APPEND_DOCS.items():
        path = root / rel
        if not path.is_file():
            raise ValueError(f'Missing expected documentation: {rel}')
        before = path.read_text(encoding='utf-8')
        if marker not in before:
            changes[path] = before.rstrip() + '\n\n' + text + '\n'
    if not changes:
        print('ALREADY APPLIED: modern onboarding UI patch found. No files changed.')
        return 0
    stamp = datetime.now().strftime('%Y%m%d-%H%M%S')
    backup = root.parent / f'sira-ba-before-modern-ui-{stamp}.zip'
    with zipfile.ZipFile(backup, 'x', compression=zipfile.ZIP_DEFLATED) as zf:
        for p in changes:
            if p.is_file():
                zf.write(p, p.relative_to(root).as_posix())
    for p, new in changes.items():
        p.parent.mkdir(parents=True, exist_ok=True)
        fd, temp = tempfile.mkstemp(prefix='.siraba-ui-', dir=p.parent)
        try:
            with os.fdopen(fd, 'w', encoding='utf-8', newline='\n') as f:
                f.write(new)
            os.replace(temp, p)
        finally:
            if os.path.exists(temp):
                os.unlink(temp)
    print(f'PATCHED: {len(changes)} UI/docs files')
    print(f'BACKUP (no credentials/git): {backup}')
    print('NEXT: cd android-app && ./gradlew :app:testDevDebugUnitTest :app:assembleDevDebug :app:installDevDebug --no-daemon')
    print('STATUS: Android build and device visual check after this patch are UNVERIFIED')
    return 0

if __name__ == '__main__':
    cli = argparse.ArgumentParser(description=__doc__)
    cli.add_argument('--root', type=Path, default=Path.cwd())
    options = cli.parse_args()
    try:
        raise SystemExit(run(options.root))
    except Exception as exc:
        print(f'ERROR: {exc}')
        raise SystemExit(1)
