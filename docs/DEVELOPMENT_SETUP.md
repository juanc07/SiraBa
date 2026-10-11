# Development setup and environment isolation

## Prerequisites

Android Studio compatible with AGP 9.0.1 + Android SDK Platform API 36 + Build Tools 36.0.0, JDK 21, Node 22, Firebase CLI (Node 22 compatible), local Android emulator/device, `kotlinc` for independent domain check, npm. Internet is required once to resolve Gradle plugins/AndroidX/Firebase packages and install Firebase CLI/tests. Do not use an old Radiant Circle Firebase project.

## Initialize Git

```bash
unzip sira-ba-foundation.zip
cd sira-ba
git init -b main
git add README.md docs android-app backend firebase tests scripts firebase.json .github .gitignore .firebaserc.example
git status --short
git commit -m "chore: bootstrap Sira Ba foundation (unverified Android/Emulator gates)"
```

Use `feature/<issue>` branches, reviewed PRs to protected main, commit only selected files, never `git add .` before checking source state. Keep reference source artifacts only in external immutable Project resources.

## New Firebase projects — do not guess IDs

1. In Firebase Console create **three separate new projects** for `DEV`, `STAGING`, `PROD` (do not use old Radiant projects). Set restricted IAM, spend/billing alerts, retention and location appropriately.
2. Register *new* Android app IDs: `net.thinkbloxph.siraba.dev` for DEV, `net.thinkbloxph.siraba.staging` for STAGING, `net.thinkbloxph.siraba` for PROD. Reconfirm unique package naming before public release.
3. Download respective `google-services.json` only from matching project's Firebase Console and save locally at:
   - `android-app/app/src/dev/google-services.json`
   - `android-app/app/src/staging/google-services.json`
   - `android-app/app/src/prod/google-services.json`
4. Create `.firebaserc` locally from `.firebaserc.example`; replace placeholders with new actual Firebase project IDs. `.firebaserc` is gitignored to prevent deploying to wrong projects accidentally.
5. Register App Check DEBUG tokens only in DEV (from Android debug log) and configure Play Integrity for each app identity before production. Do not commit debug tokens.
6. Select Firestore/Storage regions deliberately. Configure Firebase Authentication providers (later Phase 2); never deploy the web frontend or provision production admin SDK keys in Android.
7. During Phase 1 no actual customer/worker production data is authorized; **do not deploy** backend/rules to production before approval.

## Build with or without DEV Firebase config

```bash
cd android-app
./gradlew :app:testDevDebugUnitTest :app:assembleDevDebug
# App is only a UI preview without DEV google-services.json
./gradlew :app:installDevDebug
# connected device/emulator only:
./gradlew :app:connectedDevDebugAndroidTest
```

If `src/dev/google-services.json` is supplied, `assembleDevDebug` applies Google Services for that variant. Any extra variant (`staging`, `prod`) requires its own correctly matched JSON, not a copied DEV config. Google authentication/OTP screens are **not** implemented, even with Firebase configured.

Android emulators (not physical phones) may opt into local Firebase emulator routing; start Firebase emulators first:

```bash
# from repo root, after npm dependencies / Firebase CLI installation
firebase emulators:start --project YOUR_REAL_NEW_SIRABA_DEV_PROJECT_ID --only auth,firestore,functions,storage
# separate terminal
cd android-app
./gradlew :app:installDevDebug -PuseFirebaseEmulators=true
```

For Android emulators, the emulator CLI project ID must match `src/dev/google-services.json`; `demo-siraba` is reserved for isolated Firebase rule tests and will not match a real DEV app JSON. `10.0.2.2` points to host on Android emulator. Physical devices need deliberate `adb reverse`/network configuration, **not** production endpoint substitution.

## Backend tests and compilation

```bash
npm --prefix backend/functions install --no-audit --no-fund
npm --prefix backend/functions run check
npm --prefix tests/firebase install --no-audit --no-fund
./scripts/test-firebase.sh
```

**Lockfile gap:** package lockfiles have not been generated because this execution environment cannot download npm packages. Generate and commit `package-lock.json` for both npm packages, then migrate CI to `npm ci` before trusting reproducible CI. This is a Phase 1 blocker.

## Source and security checks

```bash
./scripts/test-domain.sh
python3 scripts/verify-repository.py
# git state available after init
git status --short
git diff --check
```

Regex source scanning finds obvious leaks only; before any external repository/public release use approved full-history secret scan, Firebase IAM audit and deploy identity review. Avoid terminal screenshots displaying configuration values.
