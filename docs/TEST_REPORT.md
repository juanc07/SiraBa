# Phase 1 test report — evidence ledger

Date: 2026-10-11. Machine: Linux container, Java 21, Node 22.16.0, global Kotlin CLI and TypeScript CLI available. No Android SDK/adb, Gradle distribution, Firebase CLI nor access to external Maven/Gradle/npm downloads; no configured Sira Ba? Firebase project.

## Actually executed

| Command | Result | Evidence |
|---|---|---|
| `./scripts/test-domain.sh` | PASS | Kotlin compiled via local `kotlinc`; six assertion checks for three categories/unsafe categories and worker preview eligibility |
| `npm --prefix backend/functions run test:policy` | PASS | Node `--experimental-strip-types`: 4 tests, 0 failures; fee always 0 for valid labor, invalid centavos rejected, free policy immutable, mutation rejected |
| `python3 scripts/verify-repository.py` | PASS (static scope only) | Simple scan for local secret file names, old app identities, obvious key fragments, both Firebase rules deny all |
| `pdftotext`, `python-docx`, reference ZIP extraction/inspection | PASS | Static document and source audit; not proof of code correctness |

## Not executed — UNVERIFIED

| Command / gate | Why unavailable |
|---|---|
| `android-app/gradlew :app:assembleDevDebug` | Android SDK and Gradle distribution not installed; outbound DNS blocked |
| `android-app/gradlew :app:testDevDebugUnitTest` | Same issue; JUnit tests are authored but not run |
| `android-app/gradlew :app:connectedDevDebugAndroidTest` | No emulator / adb or installed APK |
| Firebase SDK DEV startup / Auth sign-in | No new DEV Firebase project/config |
| `npm --prefix backend/functions run build` | npm libraries absent; no external dependency resolution |
| `./scripts/test-firebase.sh` | Firebase CLI and `@firebase/rules-unit-testing` unavailable; auth/firestore/storage emulators not started |
| Remote GitHub Actions workflow | No remote repository connected/run |
| App Check token / Play Integrity / Crashlytics delivery | Cannot validate without configured Firebase project and device |
| Comprehensive leakage and secrets audit | Only static source scan performed; history, binary and external security audit pending |

## Manual QA and negative tests to capture later

- Fresh DEV install and welcome display; customer/worker navigation + Back; small portrait phone, screen reader, large text, slow network.
- Missing Firebase config shows honest offline preview without crash or fake signed-in state; configured DEV initializes Auth locally and shows signed-out state (server reachability still needs a separate check) with **no** pretend booking.
- Emulator guest user cannot read/write `users`, `workerVerifications`, `bookings`, `billingPolicies`, `evidence`; customer/worker cannot impersonate other parties; all Storage read/write denied.
- Forged client paid flag must not create a fee: no payment callable is exported and free policy always returns 0.
- App Check required by deployed callable; invalid/absent auth denied; check server and client SDK version compatibility in new DEV project.
- Ensure first real client rule openings are tested against denied reads and unauthorized writes **before** deploying.

Do not upgrade UNVERIFIED entries to PASS based on authored code or an emulator test definition. Include date, exact command, device/emulator API and logs for future results.

## SB-P1-001 validation work — 2026-10-11 (incremental)

| Test | Outcome | Exact scope |
|---|---|---|
| `sh -n android-app/gradlew` | PASS | Unix shell syntax after LF conversion |
| `bash -n scripts/test-domain.sh scripts/test-firebase.sh` | PASS | Runner syntax only |
| `cd android-app && ./gradlew --version` | BLOCKED | Gradle wrapper now launches correctly, attempts distribution download; `UnknownHostException: services.gradle.org`, no Gradle itself executed |
| `python3 -m unittest discover -s tests/repository -p 'test_*.py'` | PASS | 5 offline synthetic Firebase config tests |
| `python3 scripts/validate_firebase_config.py` | PASS (static/optional) | No JSON present; confirms preview mode; **does not** prove DEV Firebase connected |
| `python3 scripts/verify-repository.py` | PASS (static) | Obvious patterns only and launcher line endings |
| GitHub Actions | UNVERIFIED | No remote repository/CI run |
| Android APK/emulator/App Check/DEV/Firebase rules | UNVERIFIED | Missing machine SDK/cloud access/emulator dependencies |

This work does not change Phase 1 acceptance status. The full Windows execution procedure is in `SB-P1-001_VALIDATION_RUNBOOK.md`.

## SB-P1-001 continued — validated 2026-10-11, local container

**Portable runner**: `python3 scripts/validate_phase1.py` executed, with evidence in
local gitignored `validation-results/validation.json`. The runner exited **2** meaning
some gates are blocked/unverified, **not** that Phase 1 passed.

| Evidence | Actual status | Interpretation |
|---|---|---|
| `scripts/verify-repository.py` | PASS | Basic static source/credentials guard only |
| Python Firebase config validator tests | PASS — 6 | Includes wrong package, reused project, reference ID and incomplete file rejection |
| Firebase local mapping validator | PASS — no configs present | Allows missing JSON; **does not validate Firebase DEV** |
| Kotlin CLI domain compilation and smoke | PASS — 6 assertions | Compile/run via host `kotlinc`, not Android Gradle/JUnit |
| Backend payment policy | PASS — 4 tests | Native Node 22 TS-stripping policy tests |
| Backend SDK TypeScript full build | UNVERIFIED | npm SDK dependencies not downloaded |
| `./gradlew :app:testDevDebugUnitTest :app:assembleDevDebug` | BLOCKED | Gradle distribution host DNS unavailable (`UnknownHostException`) |
| Android instrumentation and manual screen tests | UNVERIFIED | No AVD/adb installed here |
| Firebase Emulator security tests | UNVERIFIED | Firebase CLI/rules test dependencies unavailable |
| DEV Firebase initialization and authenticated API | UNVERIFIED | Firebase DEV Console project/configuration not provided |
| GitHub remote repository/Actions | UNVERIFIED | No GitHub connector operations exposed in this execution; no push or CI run |

Local cross-platform Gradle launcher was statically inspected and is LF-normalized; no claim
of Gradle compilation is made. Source is packaged without local test logs or bytecode caches.
**Phase 1 NOT ACCEPTED.**

## SB-P1-005 — Intro and branding validation
Unit/source checks provided in `tests/repository/test_intro_assets.py` (run with `python -m unittest discover -s tests/repository -v`). Real `assembleDevDebug`, `testDevDebugUnitTest`, installation, and Compose UI navigation must run on development phone; report as UNVERIFIED until then.

## SB-P1-006 — Visual-reference UI source validation
- Source regression checks: `python -m unittest discover -s tests/repository -v` (16 tests passed in isolated container).
- PNG signature checks for new locally bundled intro/role artwork: PASS.
- Android build, Android instrumentation and visual acceptance on user's Samsung device after SB-P1-006: **UNVERIFIED**.
- Firebase DEV live connectivity, rules emulator security checks, GitHub CI: **UNVERIFIED**.
- Do not claim screenshots of mockup are real app device screenshots; compare actual installed app to supplied reference.
