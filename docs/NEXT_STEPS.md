# Next recommended task and acceptance protocol

**Detailed Windows runbook:** [SB-P1-001_VALIDATION_RUNBOOK.md](SB-P1-001_VALIDATION_RUNBOOK.md). Use the corrected 2026-10-11 SB-P1-001 ZIP, not the previous bootstrap snapshot.

**Run from Windows repository root after extracting latest package:**
`python scripts/validate_phase1.py --require-dev --with-device --with-emulators`
(install the local requirements and start the emulator first). This produces the gitignored
`validation-results/validation.json` evidence ledger; it never deploys to Firebase.

## Task SB-P1-001: Verify foundation end to end (do not start Phase 2)

1. On an Android Studio machine, install JDK 21, Android SDK 36/Build Tools 36, Node 22, Firebase CLI, and resolve dependencies.
2. Run `./scripts/test-domain.sh`, `python3 scripts/verify-repository.py`, and backend pure policy tests. Fix any new failures before connecting real projects.
3. From `android-app/`, run `./gradlew :app:testDevDebugUnitTest :app:assembleDevDebug` and record artifact path and SHA256. Address dependency/AGP/manifest incompatibilities with **focused patches**, not broad refactors.
4. Launch `installDevDebug` on a named emulator/API and run `connectedDevDebugAndroidTest` to validate welcome, customer, worker and Back; take screenshots/record text-scale issues.
5. Create a completely new Firebase **DEV** project; register `net.thinkbloxph.siraba.dev`, add local matching `src/dev/google-services.json` and run the app to verify Firebase initialization, Auth signed-out state, and actual DEV backend reachability (not merely local initialization). Do **not** put private service-account material in APK or Git.
6. Install backend/emulator package dependencies; generate/commit lockfiles; run `npm run check`, `./scripts/test-firebase.sh`, capture failing-denied assertions and emulator logs.
7. Ensure Dev App Check debug token registered and only emulator/test accounts used; validate read-only callable after DEV Auth configured (Phase 2 sign-in flow not required for this phase if using secure test fixture); confirm no privileged writes.
8. Push to a **new** GitHub repository, protect main, run `.github/workflows/verify.yml`, review logs. Harden release/CI versions as needed and re-run all gates.
9. Update `TEST_REPORT.md`, `PHASE_STATUS.md`, `CHANGELOG.md`, `DECISION_LOG.md` and handoff with actual results. Accept Phase 1 only after all mandatory gates pass.

## Future development order (blueprint)

Phase 2: identity/account lifecycle and server-authorized roles. Phase 3: worker verification and restricted ID uploads. Phase 4: photo request and eligible worker search. Phase 5: itemized quotes. Phase 6: booking and address reveal. Phase 7: completion evidence/dispute. Phase 8: disabled payment-ready contract expansion. Phase 9–11: operations, closed beta, FREE pilot. Phase 12: paid activation **only after explicit approvals**.

## Explicit no-go conditions

No new privileged Firestore rule opening, no production collection of personal documents, no live payment capture, no silent default admin/verified worker claims, no copying Radiant secrets/identities, no Phase 2 development until Phase 1 test gate is satisfied.

## SB-P1-005 — On-device verification
Build and install DEV APK on Samsung; confirm generated Sira Ba? logo, first-run Magsimula → Welcome, Customer/Worker navigation and returning-user skip. Take screenshots, test accessibility. Firebase emulator/security gates still outstanding.

## SB-P1-006 — Next device action
Download `apply_siraba_design_match.py` to Windows Downloads. From `C:/2026/SiraBa/sira-ba`, execute `python C:/Users/USER/Downloads/apply_siraba_design_match_fixed.py --build-install` with a connected device. Open installed DEV app and verify the intro appears, Magsimula opens illustrated Customer/Worker welcome, buttons navigate, backgrounding/reopening or closing/relaunching the app shows intro again. Send device screenshots and build logs before marking approved. Do not change Firebase or payments.
