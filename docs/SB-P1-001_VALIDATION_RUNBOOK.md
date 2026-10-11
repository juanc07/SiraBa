# SB-P1-001 — Windows Android Studio + Firebase DEV validation

**State:** LOCAL VALIDATION NOT YET PERFORMED. This is a procedure, not a record of successful tests.
**Source authority:** Sira Ba? Master Project Blueprint v1.0, immutable Android/Firebase Operating System, this repository. Keep the original Radiant Circle ZIP and blueprint files outside this repository.
**Scope:** Build and test foundation; provision **new DEV only**; no real bookings or paid services; do not start Phase 2 until the gate passes.

## A. Prepare Windows development machine

1. Extract the newest `sira-ba-*.zip` so the path resembles `C:\2026\SiraBa\sira-ba\`. Make sure `C:\2026\SiraBa\sira-ba\android-app\settings.gradle.kts` exists. Never import Radiant Circle as this app's base.
2. Install Android Studio that supports Android Gradle Plugin (AGP) 9.0.1. In Android Studio **Settings > Languages & Frameworks > Android SDK** install **Android SDK Platform 36**, **Build Tools 36.0.0**, **Platform Tools**, **Android Emulator**, and one **Google APIs/Google Play** x86_64 or arm64 image that matches your CPU.
3. In Android Studio **Settings > Build, Execution, Deployment > Build Tools > Gradle**, select an installed **JDK 21** for Gradle (JDK 17 is the AGP minimum, but this repository/CI selects 21). Gradle wrapper 9.3.0 is supplied; do not replace it with random online scripts.
4. Install Node.js **22**, Python **3.11+** and Firebase CLI (`npm install -g firebase-tools`). Keep Git installed. Verify in Windows **PowerShell**:

   ```powershell
   java -version
   node --version
   npm --version
   python --version
   firebase --version
   ```

5. Start Android Studio; select **Open** and pick the **`C:\2026\SiraBa\sira-ba\android-app`** folder, **not** the zip or the repository root. Allow Gradle Sync/SDK downloads. For CI-like tasks, use the terminal commands below.

## B. First build: no Firebase cloud connection yet

**Recommended single command from the repository root** (works in Windows PowerShell):

```powershell
python scripts/validate_phase1.py
```

This records test names, results and local command logs under the gitignored `validation-results/`
folder. A `BLOCKED` result is not a passed test. Exit code 2 means incomplete/unverified,
exit code 1 means at least one actual test failure. To request all automated local gates after setup:

```powershell
python scripts/validate_phase1.py --require-dev --with-device --with-emulators
```

If you prefer individual commands, use the examples below. Remote GitHub CI, manual
navigation and actual DEV authentication must be recorded separately.

From Windows PowerShell, replace `C:\dev` with your extraction folder.

```powershell
cd C:\2026\SiraBa\sira-ba
python scripts\verify-repository.py
python -m unittest discover -s tests\repository -p 'test_*.py'
python scripts\validate_firebase_config.py
npm --prefix backend\functions run test:policy
cd android-app
.\gradlew.bat :app:testDevDebugUnitTest :app:assembleDevDebug --stacktrace
```

**Expected if successful:** `BUILD SUCCESSFUL`; APK in `android-app\app\build\outputs\apk\dev\debug\`. Before configuration, this is a limited UI preview. **Do not mark it PASS until actually executed.** If sync fails, save the **first real Gradle error** (not merely the last stack-trace line), the IDE/Gradle/Java versions and the command run. Never paste `google-services.json` or tokens into chats/logs.

## C. Android Emulator and Compose UI

1. Android Studio **Tools > Device Manager > Create device**. Choose a normal-sized portrait Pixel/phone and API 35 or API 36. Start the AVD.
2. In Android Studio, set **Build Variants** to `devDebug`. Choose the AVD and press **Run**. Confirm the app shows **SIRA BA?**, slogan, free-booking notice, and clearly indicates that it is a development foundation.
3. Click **May ipapaayos ako**, then back; click **Trabahador ako**, then back. Verify there are no actual booking, payment, verified-worker, or wallet controls.
4. From PowerShell inside `android-app`:

   ```powershell
   .\gradlew.bat :app:installDevDebug :app:connectedDevDebugAndroidTest --stacktrace
   ```

5. Record emulator model/API, `Build SUCCESSFUL` (or exact failure), screenshot of welcome/navigation and instrumentation test count. Repeat a simple manual check on a small portrait screen/large text.

## D. Create **fresh** Sira Ba? Firebase DEV (account-owner action)

1. Go to [Firebase Console](https://console.firebase.google.com/), sign in to the **account that should own Sira Ba?**, and create a **new** Firebase project specifically for **DEV**. Do **not** choose an existing Radiant Circle project. Name it recognizably (`Sira Ba Development`), but the exact Firebase project ID is generated/selected by the Console.
2. Register an **Android app** with **exact** Android package ID: **`net.thinkbloxph.siraba.dev`**. Display name can be `Sira Ba DEV`. Do **not** use the final production package ID `net.thinkbloxph.siraba` for the DEV app. Download this new Android app's `google-services.json`.
3. Save the downloaded file at **`C:\2026\SiraBa\sira-ba\android-app\app\src\dev\google-services.json`** (create `src\dev` only if needed). Never put it under `src\main`, commit it, or reuse it for other environments.
4. Review Firestore database/storage region and account permissions/cost alerts before creating services. Avoid uploading real IDs, photos, addresses or customer data for Phase 1. Billing/Blaze may be necessary for some Cloud Functions/Storage provisioning; inspect Firebase's current cost requirements before enabling billing. **Do not deploy production resources.**
5. In PowerShell at repo root:

   ```powershell
   python scripts\validate_firebase_config.py --require-dev
   git status --short
   git check-ignore android-app/app/src/dev/google-services.json
   ```

   The script checks file structure/package/project segregation only. `git check-ignore` should output the DEV JSON path (after Git initialization). Don't publish the JSON merely because it is often described as containing non-secret identifiers; retain the project's stricter local-config policy.
6. Build/run the **devDebug** variant again. Observe a signed-out Firebase session in the UI (not an actual Google/OTP sign-in). This confirms the Android initialization path, **not** a completed DEV backend reachability/authentication test. Record actual Logcat outcome; full remote Firebase/Auth/App Check connectivity remains a separate gate.
7. For a direct check that the newly created Firebase project exists and your account can access it, authenticate the **Firebase CLI** with `firebase login`, then `firebase projects:list`. This validates CLI account access, **not** the APK. Do not deploy functions to DEV merely to make this step appear green.

## E. Firebase Emulator Suite deny-all security tests

From PowerShell at repository root, with Node 22, Java 21 and Firebase CLI installed:

```powershell
npm --prefix backend\functions install --no-audit --no-fund
npm --prefix backend\functions run check
npm --prefix tests\firebase install --no-audit --no-fund
firebase emulators:exec --project demo-siraba --only auth,firestore,storage "npm --prefix tests/firebase test"
```

`demo-siraba` is a synthetic emulator-only project ID. This command should not contact your live DEV, STAGING or PROD database. The emulator tests exercise denied guest/customer/worker operations in Firestore and Storage. Record process exit code, number of successful assertions and any emulator warnings. A failed permission assertion must be investigated; do not relax rules to make the test pass.

To test Android Firebase Emulator routing **separately**, launch local emulators with the **same project ID present in DEV's `google-services.json`** instead of `demo-siraba`, then run:

```powershell
cd C:\2026\SiraBa\sira-ba\android-app
.\gradlew.bat :app:installDevDebug -PuseFirebaseEmulators=true
```

Android's `10.0.2.2` points to the host emulator. Never use `-PuseFirebaseEmulators=true` with staging/prod, and never claim App Check/token/Cloud Function connectivity without a successful authenticated test. The current app contains no real sign-in flow; any authenticated callable round-trip requires a documented DEV-only test fixture or the Phase 2 sign-in work.

## F. Reproducible backend/CI gate

1. After `npm install`, **commit the newly generated `package-lock.json`** in `backend/functions` and `tests/firebase`. Change CI install commands from `npm install` to `npm ci` **in a reviewed follow-up patch** and re-run. Do not fabricate lockfiles or claim a clean install while network/package resolution is unavailable.
2. Initialize a **new** private Git repository if needed, push the corrected code to a **new** GitHub project, protect `main` and run `.github/workflows/verify.yml`. Review the first failing step before making any unrelated changes.
3. No production deploy, public beta, real customer collection, or release APK until permissions, privacy and payment gates are approved independently.

## G. Evidence to paste into the handoff (no secrets)

```text
SB-P1-001 date/time/timezone:
Machine OS, Android Studio, Java, Node, Gradle versions:
Source commit SHA and git status --short:
Android Gradle sync: PASS/FAIL/UNVERIFIED (evidence):
:app:testDevDebugUnitTest: PASS/FAIL/UNVERIFIED (count):
:app:assembleDevDebug: PASS/FAIL/UNVERIFIED (APK path + SHA256):
Emulator model + Android API:
:app:connectedDevDebugAndroidTest: PASS/FAIL/UNVERIFIED (count):
DEV config validator: PASS/FAIL/UNVERIFIED (do not paste JSON):
DEV SDK initialization: PASS/FAIL/UNVERIFIED:
DEV server reachability/Auth/App Check: PASS/FAIL/UNVERIFIED:
Firebase Emulator rules tests: PASS/FAIL/UNVERIFIED (test count):
Backend functions npm build: PASS/FAIL/UNVERIFIED:
GitHub Actions run URL/status:
Secrets/security findings:
Next specific blocker/fix:
```

**Phase 1 acceptance:** Requires real Android build, actual emulator/device navigation, verified DEV connection and emulator negative tests, no secret disclosure, all required automated gates passed and handoff updated. Incomplete gates remain **UNVERIFIED**; do not start Phase 2.
