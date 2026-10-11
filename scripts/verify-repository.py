#!/usr/bin/env python3
"""Fast tracked-source guard; NOT a comprehensive secret scanner."""
from pathlib import Path
import re, subprocess, sys
root = Path(__file__).resolve().parents[1]
forbidden_files = re.compile(r'(^|/)(google-services\.json|local\.properties|.*service.account.*\.json|.*firebase-adminsdk.*\.json|.*\.(jks|keystore|pem|p12)|\.env(?:\.[^/]+)?)$',re.I)
excluded = {'.git', '.gradle', 'build', 'node_modules', 'lib'}
violations=[]
tracked = subprocess.run(['git','-C',str(root),'ls-files','-z'],capture_output=True)
if tracked.returncode == 0 and tracked.stdout:
    candidates=[root/p.decode() for p in tracked.stdout.split(b'\0') if p]
else:
    # Before git init, files excluded by gitignore are local and not archive inputs.
    candidates=[f for f in root.rglob('*') if f.is_file()]
for f in candidates:
    if not f.is_file() or any(x in excluded for x in f.relative_to(root).parts):
        continue
    rel=f.relative_to(root).as_posix()
    if forbidden_files.search(rel) and not rel.endswith('.env.example'):
        # Local configs are expected when developing; only tracked configs are an error.
        if tracked.returncode == 0 and tracked.stdout:
            violations.append(f'Sensitive file TRACKED by Git: {rel}')
        continue
    if rel.endswith(('.kt','.kts','.ts','.js','.json','.md','.xml','.yml','.toml')):
        txt=f.read_text(errors='replace')
        if re.search(r'com\.thinkblox\.radiantrush|radiant-rush-10a9c|BEGIN PRIVATE KEY|AIza[0-9A-Za-z_-]{25,}|sk-[0-9A-Za-z_-]{20,}',txt):
            violations.append(f'Old app identity or private key fragment: {rel}')
for expected in ['firebase/firestore.rules','firebase/storage.rules']:
    txt=(root/expected).read_text()
    if 'allow read, write: if false' not in txt:
        violations.append(f'Unexpected missing deny-all rules: {expected}')
# Do not regress into a CRLF shebang that breaks Linux/GitHub CI.
gradle_shell = (root/'android-app/gradlew').read_bytes()
if b'\r\n' in gradle_shell:
    violations.append('Unix Gradle wrapper contains CRLF line endings')
if not (root/'.gitattributes').exists():
    violations.append('Missing .gitattributes to preserve Gradle LF endings')
if violations:
    print('FAILED:')
    for message in violations: print('-',message)
    sys.exit(1)
print('PASS: obvious tracked credentials/old identities absent, initial rules deny access (static scope only).')
