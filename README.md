# Sira Ba? — Picture-an. Presyuhan. Paayos.

Native Kotlin Android + Firebase foundation for a Philippine hyperlocal **small-repair** marketplace.

**Status as of 2026-10-11:** Phase 0 resource audit complete; Phase 1 code scaffold prepared; **Phase 1 NOT accepted**. Android APK/device, Firebase DEV connection, Firebase Emulator security suite and CI execution are **UNVERIFIED**. Do not use this source for a public pilot.

## Source authority
1. **Product:** Sira Ba? Master Project Blueprint v1.0 (2026-10-11) — external immutable Project resource.
2. **Engineering:** Android Kotlin Firebase Operating System from Radiant Circle — external immutable Project resource.
3. **Reference only:** Radiant Circle final reference ZIP — immutable outside this repository, **never a dependency**.
4. **Implementation:** this repository, its commit and current test evidence.

The earlier developer guide's mention of worker-paid credits is **superseded** by the Sira Ba? blueprint's future *customer-paid* 5%-of-labor model. All bookings in the approved initial launch remain FREE; no payments, credit purchases, or checkout code is present.

## Repo layout

```text
sira-ba/
├── android-app/        Gradle root + :app (Compose, MVVM, pure Kotlin domain)
├── backend/functions/  Firebase Cloud Functions Gen 2 in TypeScript; currently READ-ONLY
├── firebase/           Firestore/Storage security rules and indexes (DENY ALL in Phase 1)
├── tests/              Firebase Emulator rules suite and standalone Kotlin smoke
├── scripts/            Source guards and test runners
├── docs/               Audit, architecture, setup, decisions, status, QA evidence
├── .github/workflows/  Proposed CI gates (not yet run remotely)
└── firebase.json       Emulator wiring; no live Firebase IDs
```

## One-command validation (Windows, macOS, Linux)

From a terminal at the repository root:

```powershell
python scripts/validate_phase1.py
```

On Windows with DEV Firebase Android configuration saved locally, an Android emulator running, and the
Firebase CLI/test dependencies installed:

```powershell
python scripts/validate_phase1.py --require-dev --with-device --with-emulators
```

The script **does not deploy or collect live user data**. It writes local `validation-results/validation.json`
and logs (gitignored). Return code `2` means required environment steps remain **BLOCKED/UNVERIFIED**;
return code `1` indicates at least one failed test. A local script can never confirm manual UI usability,
remote Firebase access or a successful GitHub Actions run on its own. Follow the [validation runbook](docs/SB-P1-001_VALIDATION_RUNBOOK.md).

## Local quick start

Prerequisites: JDK 21, Android Studio with API 36 SDK/build-tools, Node 22, Kotlin compiler for standalone smoke test; Gradle wrapper is included. See [full environment setup](docs/DEVELOPMENT_SETUP.md) and the [Windows-focused Phase 1 validation runbook](docs/SB-P1-001_VALIDATION_RUNBOOK.md).

```bash
./scripts/test-domain.sh
npm --prefix backend/functions run test:policy
python3 scripts/verify-repository.py
python3 scripts/validate_firebase_config.py  # --require-dev after configuring Firebase
python3 -m unittest discover -s tests/repository -p 'test_*.py'
cd android-app
./gradlew :app:testDevDebugUnitTest :app:assembleDevDebug
```

`assembleDevDebug` builds an **unconfigured UI preview** until you create a *new* Firebase DEV project and place a locally downloaded JSON at `android-app/app/src/dev/google-services.json` (gitignored). It does not imply sign-in or bookings work. Other variants need independent JSON files under `src/staging` and `src/prod`.

To test Firebase rules after installing Firebase CLI and emulator-test npm dependencies:

```bash
npm --prefix tests/firebase install
./scripts/test-firebase.sh
```

For an Android emulator connected to *only* the DEV Firebase Emulator Suite, start the host suite with the **same project ID as the DEV `google-services.json`** (not the independent `demo-siraba` rules-test ID):

```bash
firebase emulators:start --project YOUR_REAL_NEW_SIRABA_DEV_PROJECT_ID \
  --only auth,firestore,functions,storage
# separate terminal
cd android-app
./gradlew :app:assembleDevDebug -PuseFirebaseEmulators=true
```

The Android guest uses `10.0.2.2` for the host machine. Production/staging builds never route to emulators. Use Firebase `demo-siraba` project ID for local rule tests only, never for production deployments.

## Required next gates

- Configure a newly created DEV Firebase project and download matching `google-services.json`.
- Execute the Android build and unit test on a machine with Android SDK / Gradle distribution resolution.
- Launch on emulator/device and run `connectedDevDebugAndroidTest`.
- Install Firebase CLI and packages, execute all rules tests against local emulators.
- Confirm GitHub CI passes, revisit privacy consent and App Check configuration before any live use.

Detailed current evidence: [TEST_REPORT](docs/TEST_REPORT.md); execution blockers: [NEXT_STEPS](docs/NEXT_STEPS.md).

## Application identity (ThinkBlox PH)

Android production ID: `net.thinkbloxph.siraba`; DEV: `net.thinkbloxph.siraba.dev`; STAGING: `net.thinkbloxph.siraba.staging`. For Windows use `C:\2026\SiraBa\sira-ba`. Firebase projects and per-flavor Android registrations remain separate and manual. See [package migration guide](docs/PACKAGE_ID_MIGRATION.md).
