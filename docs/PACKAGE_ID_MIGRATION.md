# SB-P1-002 — Android package & Firebase identity migration

**Decided 2026-10-11:** The ThinkBlox PH website domain is `thinkbloxph.net`. Android package names use reverse DNS order, so Sira Ba?'s Kotlin namespace and production application ID are `net.thinkbloxph.siraba` — **not** `thinkbloxph.net`.

| Flavor | Android application ID | Firebase environment |
|---|---|---|
| DEV | `net.thinkbloxph.siraba.dev` | Separate Sira Ba? DEV project |
| STAGING/UAT | `net.thinkbloxph.siraba.staging` | Separate Sira Ba? STAGING project |
| PROD | `net.thinkbloxph.siraba` | Separate Sira Ba? PROD project |

Kotlin sources and instrumentation/unit tests live under `android-app/app/src/<sourceSet>/java/net/thinkbloxph/siraba/`. The Android manifest uses relative `.MainActivity` and `.SiraBaApplication` class names; these resolve from the Gradle `namespace`.

## Existing Windows working directory

`C:\2026\SiraBa\sira-ba`

If you have an older extracted repository, save your work first and run the provided one-time migration utility **from the repository root**. It creates an external backup before patching. Do not simply unzip over the old tree because duplicate old and new Kotlin package directories would remain.

## Firebase Console instructions

1. Do not change or reuse any Radiant Circle Firebase application or project. Create **Sira Ba? DEV** as an independent Firebase project if it does not already exist.
2. In that project, register an **Android application** with **exact package ID** `net.thinkbloxph.siraba.dev`. Download a new **matching** `google-services.json` and place it locally at `android-app/app/src/dev/google-services.json` (excluded from Git).
3. If you previously registered a Firebase Android app using the provisional Sira Ba? `ph.siraba.app.dev`, its Firebase package name cannot simply be edited to the new one: register a **new Android app** under the intended DEV project, and replace the **local DEV** JSON file. Do not edit the JSON by hand. You do not need to create a second DEV Firebase project solely to correct a mistakenly registered Android app; the project itself must remain distinct from STAGING and PROD.
4. When needed, independently register `net.thinkbloxph.siraba.staging` in the STAGING project and `net.thinkbloxph.siraba` in PROD. Keep each JSON only in its matching source set, never in `src/main`.
5. Update any Firebase Auth provider-specific configuration (e.g. Google Sign-In OAuth clients), SHA-1/SHA-256 app signing fingerprints, App Check registrations, and authorized domains **for the new application ID** as those features are implemented. The Firebase Auth domain whitelist and website hosting domain are separate settings from an Android package name.
6. From `C:\2026\SiraBa\sira-ba` run `python scripts/validate_firebase_config.py --require-dev` after installing a real DEV JSON. This static validation does **not** prove runtime network access, device authorization, or deployment readiness.
7. Select `devDebug`, run Gradle unit tests and APK build, install on an emulator, verify Firebase initialization, and execute Firebase Emulator rules tests. Mark unexecuted tests UNVERIFIED.

Changing the Android application ID is a **new app identity**, so it will install separately from any earlier `ph.siraba.app.*` build, and does not automatically migrate existing installed app data or Firebase login associations. No production release is known to have been published, but check Play Console identities before distributing.

**Free launch invariant:** `paymentsEnabled=false`, payment mode `DISABLED`, effective booking fee = zero; proposed future 500 bps (5%) remains inactive and server-authorized only.
