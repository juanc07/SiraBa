# SB-P1-003 — Crashlytics Build ID Startup Fix

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
