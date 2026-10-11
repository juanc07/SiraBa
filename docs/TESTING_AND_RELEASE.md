# Testing and release gate

**Pure domain:** `./scripts/test-domain.sh` and Gradle `:app:testDevDebugUnitTest`. **Backend:** `npm --prefix backend/functions run check`. **Rules:** `./scripts/test-firebase.sh` against local demo project. **Android UI:** `:app:connectedDevDebugAndroidTest`, plus manual low-end device and accessibility. **Build:** `:app:assembleDevDebug` and eventually release QA. **Static/secret:** `python3 scripts/verify-repository.py` plus an external history scanner before publication.

Evidence level labels: PASS only when command run successfully; STATIC CHECK for inspection/scripts only; PREPARED for authored untested code; UNVERIFIED for gates not run. See `TEST_REPORT.md`.

Release: verify `git status`, `git diff --check`, versionCode/versionName, correct Firebase project, explicit rules diff, storage privacy policy, production App Check, external-service costs and data minimization, signed artifact/keystore stored outside repo, physical-device flows, crash logs, rollback plan and support protocol. Avoid publishing merely on successful compilation.
