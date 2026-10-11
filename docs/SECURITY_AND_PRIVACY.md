# Security and privacy boundary — Phase 1

- Firestore/Storage client access: **DENY ALL** (rules files in `firebase/`). Firebase Admin SDK bypasses these rules; never trust an unauthenticated Function handler.
- Cloud Function: `getFoundationStatus` is read-only, requires Firebase Auth and App Check, rejects all data fields; `maxInstances` limits cost exposure. It is **not** a registration or booking endpoint.
- Money and worker verification: zero client authority. No checkout, credit, top-up, charge, fee toggle or gateway SDK anywhere in build. Future 5% on approved labor only is a non-activated policy proposal.
- Addresses, ID documents, skills, evidence, names and phone numbers: not collected or uploaded in Phase 1; future Storage and private projections require narrow ownership tests and access logging.
- App Check supports abuse resistance; doesn't replace UID/role/category/ownership authorization.
- Crash reporting: Firebase Crashlytics SDK integrated behind error reporter, collection deliberately **OFF** pending Philippine notice/consent review. Logs contain internal error codes only, not exception message/PII.
- Signing: SDK project config JSON treated as locally scoped and gitignored; **never** ship Admin SDK service-account files or release keystores. `google-services.json` is client configuration, not an Admin SDK secret, but each version must still be restricted to its correct environment/app.
- Future privileges: backend retrieves role/capability from trusted records or centrally issued claims, never from client supplied strings. Admin changes, ID reviews and disputes require audited workflows.
- PROD: no deployment without protected IAM, explicit project alias selection, zero-data leak negative tests, App Check verification, privacy controls, user support route and rollback evidence.

Static scans and emulator-denial test definitions are *necessary but not sufficient*. See `TEST_REPORT.md` for actual testing status.
