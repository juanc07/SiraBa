# Current developer handoff

**Exact source**: generated repository archive delivered on 2026-10-11, not yet a remote Git branch. After importing, preserve working tree/branch hash and record a first reviewed commit. Four original reference resources are immutable outside this codebase. The ZIP is a pattern library; do not add it to this repository.

**Current state:** Phase 0 source audit complete. Phase 1 foundation code scaffold exists but is NOT accepted: no successful Gradle APK build, Android launch, Firebase DEV connection, emulator rules execution or CI workflow run. Test evidence in `TEST_REPORT.md` is limited to Kotlin CLI, Node fee policy, static guard.

**Ownership:** Compose UI -> ViewModel -> AuthRepository. Kotlin `domain/rules` only local UI hints; Cloud Functions owns future privileged commands. Firestore/Storage currently deny all clients. Future paid rate 5% recorded only as disabled backend contract. Open issues and the precise next task are in `DECISION_LOG.md` / `NEXT_STEPS.md`.

**Next task:** SB-P1-001 in `NEXT_STEPS.md`; verify on SDK-capable PC, link brand-new Firebase DEV and emulator tests, record evidence. Do not start Phase 2 or collect live worker IDs prematurely.
