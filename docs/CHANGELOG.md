# Changelog

## 2026-10-11 — Foundation scaffold (not released)

- Audited four project resources and recorded reuse/conflicts/credential limitations.
- Established independent single-module Android Kotlin/Compose app with development product flavors, App Check sources, Auth observer, domain rule tests, welcome and role entry navigation.
- Created TypeScript backend read-only authenticated status callable and immutable free-fee policy, Firestore/Storage deny-all rules, emulator test definitions and GitHub CI proposal.
- Created technical, product, security, setup, decision, test and handoff documentation. No real Firebase link, production release or payment code.
- Executed six standalone Kotlin assertions, four Node backend policy tests and local static source guard. Device/SDK/emulator/CI gates remain **UNVERIFIED**.

## 2026-10-11 — SB-P1-001 local validation improvements
- Corrected `android-app/gradlew` Unix line endings and added `.gitattributes` to prevent Windows CRLF contamination. POSIX shell syntax validation passed; Gradle distribution download remains blocked by container DNS.
- Removed a premature release-variant Gradle check that would prevent DEV debug emulator builds. Build-type explicitly overrides emulator routing to `false` for every release. Real Android variant/build verification remains open.
- Added `scripts/validate_firebase_config.py` and five offline regression tests for Firebase Android package names, missing DEV config and environment isolation.
- Updated CI to run Python configuration tests and added the Windows-specific `SB-P1-001_VALIDATION_RUNBOOK.md`. No remote CI/device/Firebase project was executed.


## 2026-10-11 — SB-P1-001 validation tooling and handoff refresh

- Added Windows/macOS/Linux `scripts/validate_phase1.py` runner with local-only, gitignored command logs and a JSON results ledger.
- Strengthened Firebase Console JSON checks to reject incomplete client configuration; added a sixth synthetic config test.
- Recorded attempted Android build failure at Gradle distribution DNS lookup, while preserving Phase 1 UNVERIFIED gates.
- Preserved immutable original project references; no live Firebase projects or GitHub remotes created.

## SB-P1-002 — ThinkBlox PH package migration (2026-10-11)

- Changed namespace/application ID and Kotlin source paths to `net.thinkbloxph.siraba`; DEV `.dev` and STAGING `.staging` suffixes retained.
- Updated Firebase config mapping, unit tests and Windows setup paths. Real Firebase application registration and APK build are UNVERIFIED.

## 2026-10-11 — SB-P1-005
Added first-run logo/intro, Sira Ba? adaptive launcher icon, local-only intro-completed preference, and Compose navigation test adaptation. No payment/backend change.

## 2026-10-11 — SB-P1-006
Aligned Compose introductory and role-selection screens with approved product mockup via local artwork, gradient scene, portrait cards, real navigation and preview notices. Intro is now first route on every fresh launch and every return from background (Phase 1 preview only). Replaces previous ADR-017 first-run-only behavior. No backend, payment or Firebase identity changes.
