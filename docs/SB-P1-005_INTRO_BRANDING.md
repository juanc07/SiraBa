# SB-P1-005 — Sira Ba? intro, logo and launcher icon

Status: **SOURCE IMPLEMENTED; Android build, instrumentation, and phone visual test NOT YET VERIFIED.**

## User flow

- On a clean app-data state: Intro (Sira Ba? generated logo, ThinkBlox PH label, tagline and `Magsimula` button).
- `Magsimula`: mark intro as seen using device-local Android SharedPreferences, then navigate to existing `welcome` (Customer/Worker).
- Later launches: skip `intro` and start at `welcome`. This flag is **not** account identity, a permission, or proof of worker verification.
- Underlying Android system splash is separate and brief. Branding screen is a navigable Compose route, not a timed splash.
- Booking and worker functionality remain **preview only**; fees remain disabled.

## Brand assets

- `android-app/app/src/main/res/drawable-nodpi/siraba_brand_logo.png` — generated brand logo, used on intro.
- `android-app/app/src/main/res/drawable-nodpi/siraba_logo_mark.png` — crisp teal house/orange wrench emblem for header.
- `android-app/app/src/main/res/drawable-nodpi/siraba_launcher_foreground.png` — adaptive launcher foreground.
- `android-app/app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` — launcher configuration (minSdk 26).
- `android-app/app/src/main/res/values/colors.xml` — icon background.

## Validation on local Windows build

From `C:\2026\SiraBa\sira-ba\android-app` with Samsung connected:

```bash
./gradlew :app:testDevDebugUnitTest :app:assembleDevDebug :app:installDevDebug --no-daemon
```

On device: intro shown after updating from the previous build; logo and Magsimula visible; tap Magsimula; confirm customer and worker buttons; close/reopen app to confirm intro is skipped. Run Android instrumentation when available. Check font scale, TalkBack, portrait phone, and dark mode.

For **DEV-only** testing of a first-run experience again, clear Sira Ba? application storage using Android Settings (this also removes Firebase sign-in state and other local app data). Do **not** clear data from a production user device.

## Notes

- Graphic brand logo derived from the generated Sira Ba? concept. Edit original branding artwork for future high-resolution design iterations.
- UI-only patch: no Firestore rules, Cloud Functions, Google Services JSON, tokens, transaction state, or payment flags altered.
- A new app launcher icon is not the same as a verified Android release asset. Store listing graphics and accessibility review are separate tasks.
