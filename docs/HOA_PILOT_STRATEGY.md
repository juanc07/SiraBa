# Sira Ba? — Approved Hyperlocal HOA Pilot Strategy

**Decision ID:** SB-BIZ-001  
**Approved:** 2026-10-12  
**Decision owner:** Project founder  
**Status:** APPROVED — execution not yet started  
**Companion documents:** Sira Ba? Master Project Blueprint v1.0 (immutable reference); `docs/HANDOFF_CURRENT.md`; Android Kotlin Firebase Operating System (immutable reference)

## 1. Decision

Continue development of Sira Ba?, but **do not launch a nationwide two-sided marketplace** or spend on paid acquisition before local validation. Develop the Android app and conduct a hyperlocal, permission-based HOA pilot in parallel. First prove that a small number of available workers can complete real household micro-repairs in one defined village/subdivision; expand to nearby HOAs only after the first pilot generates evidence of reliability and demand.

This decision supplements the product blueprint; **do not overwrite or rewrite the original approved blueprint**. Preserve the product's affordable, trustworthy micro-repairs focus and its free-launch fee policy.

## 2. Why

The founder previously experienced an unsuccessful service marketplace that required a very large nationwide service-provider supply and marketing expenditure before customers could reliably use it. Sira Ba? must avoid this supply-demand cold start and protect scarce personal capital. Technical completeness is not proof of marketplace liquidity or profitable customer acquisition.

## 3. Geographic scope and services

- Pilot in **one nearby subdivision/HOA** in the founder's practical service area in Cavite; exact HOA and travel radius to be selected after outreach.
- Seek written/recorded HOA authorization before using HOA channels, implying affiliation, or coordinating worker access through subdivision gates.
- Start with **3–5 screened, reachable workers**, not 1,000 providers.
- Eligible jobs: minor water leaks, clogged sinks/toilets, simple household micro-repairs and other low-risk practical tasks, subject to worker competence and local requirements.
- Exclude major renovation, high-risk electrical/gas/structural work, and emergencies from initial scope.
- For the first demand experiment, allow **Messenger or an online request form** as the intake channel. An APK installation is *not* mandatory for residents. Move to the app once its identity, access, privacy, worker verification and booking flows are safe and tested.
- Customers pay the worker directly for agreed labor and materials. **Sira Ba? booking fee: ₱0.** No paid bookings, purchased credits, gateways, or 5% collection during this phase. Proposed future 5% of labor is *not approved for activation*.

## 4. Parallel tracks

### Track A — Engineering, gating first

1. Finish Phase 1; do not mark it accepted prematurely.
2. Resume **SB-P1-014**: authenticated Firebase `getFoundationStatus` positive test using temporary `devDebug`-only Activity. Earlier unpatched version failed safely because existing tracked `src/devDebug/AndroidManifest.xml` contained `networkSecurityConfig`; use only `apply_siraba_backend_positive_test_014_fixed.py`, which merges/restores the manifest. Do not overwrite this manifest blindly.
3. Verify response: `authenticated=true`, `bookingEnabled=false`, `billing.paymentsEnabled=false`, `paymentMode=DISABLED`, `feeAmountMinor=0`. Sign out. Remove temporary Activity and test manifest merge, then rebuild and run device tests.
4. Update Phase 1 evidence ledger, readme/status docs and GitHub Actions checks with only actually observed test results.
5. Build Phase 2 onward in blueprint order with narrow MVP scope: identity/roles, worker verification, customer photo issue, quote, booking, and completion/dispute handling; least-privilege rules; server-side approval. Avoid speculative AI/payments/nationwide expansion.
6. No public pilot using the Android app until end-to-end permissions/privacy/worker approval and production readiness have been tested.

### Track B — Business validation, start now

1. Identify **three potential nearby HOAs**, but recruit **one pilot HOA first**.
2. Speak with a decision-maker and ask permission for a limited, opt-in, independently operated micro-repair trial. Request no paid advertising and make no claims of formal HOA endorsement without approval.
3. Interview 3–5 local repair workers, check competence/reliability/references and availability; explain rates, travel charges, service boundaries and no guaranteed bookings.
4. Use manual coordination to record eligible inquiries, quotations, acceptances, cancellations, completion, issue resolution, real travel/support costs and willingness to reuse/recommend.
5. Do not collect identification documents, full addresses or repair photographs beyond what is necessary and consented to; establish privacy notice, secure storage, retention and deletion procedures before obtaining sensitive information.
6. Conduct the 30-day pilot after HOA permission and worker coverage, then evaluate before inviting second HOA.

## 5. Suggested 30-day learning goals (not forecasts)

| Metric | Initial target | Measurement |
|---|---:|---|
| HOA partners active | 1 | Explicit pilot approval |
| Reliable screened workers | 3–5 | Availability and job acceptance test |
| Genuine resident repair inquiries | 20 | Unique, eligible opt-in inquiries |
| Completed paid repairs | >=5 | Independently confirmed completion |
| Eligible-request match rate | >=70% | Matched / eligible inquiries, with definitions recorded |
| Acquisition advertising | ₱0 | No paid advertisements |
| Complaints, callbacks, unfinished jobs | Track all | Root cause, resolution and owner |
| Actual Sira Ba? revenue | ₱0 | No booking fees during free pilot |

The targets are experimental thresholds, not guarantees. A high match rate alone is insufficient if service quality is poor or the operator cannot afford coordination.

## 6. Stop / continue / expand gates

- **Stop or redesign** if worker reliability is poor, jobs do not materialize without paid acquisition, unresolved customer safety concerns appear, or coordination costs are unaffordable.
- **Continue the single-area pilot** if meaningful demand appears but workflows need refinement; prioritize fixes over new features.
- **Expand to a second nearby HOA** only with repeatable job completions, permission-based distribution, available service capacity, and acceptable quality/cost evidence.
- **Do not treat free-job activity as monetization validation**. Test customers' willingness to pay a clearly disclosed, legally compliant future service fee separately, without collecting such a fee during this pilot.

## 7. Operating and safety guardrails

- Remain on Firebase DEV while Phase 1 integration tests are being completed; Firestore/Storage deny-all rules remain enabled until narrower rules are implemented and tested.
- Firebase App Check DEV Samsung debug token is registered; never log/share/commit it. Enforcement decisions follow measured legitimate-traffic validation.
- Cloud Functions `getFoundationStatus` Gen 2, `asia-southeast1`, Node.js 22, read-only; unauthenticated HTTP request correctly returned `401` on 2026-10-11; authenticated positive request still pending as of approval.
- Keep Blaze costs controlled. Budget alerts are **not hard spending caps**; minimize deployments and retain one-day container-image cleanup.
- Worker identity/approval must not be self-attested; repair safety, pricing disclosure, support and privacy need written pilot operations procedures before live matching at scale.

## 8. Reproducible Windows / Git Bash commands

Repository root: `C:/2026/SiraBa/sira-ba` (Git Bash `/c/2026/SiraBa/sira-ba`).

```bash
cd /c/2026/SiraBa/sira-ba

# Local repository validation (no cloud deployment)
python scripts/verify-repository.py
python scripts/validate_firebase_config.py --require-dev
python -m unittest discover -s tests/repository -p 'test_*.py'

# Android build, unit tests, on-device Compose tests (Samsung A55 authorized)
cd android-app
./gradlew :app:testDevDebugUnitTest :app:connectedDevDebugAndroidTest :app:installDevDebug --no-daemon
cd ..

# Install existing last-built DEV APK without rebuilding
ADB="$(cygpath -u "$LOCALAPPDATA")/Android/Sdk/platform-tools/adb.exe"
"$ADB" devices -l
"$ADB" install -r android-app/app/build/outputs/apk/dev/debug/app-dev-debug.apk
"$ADB" shell monkey -p net.thinkbloxph.siraba.dev 1

# Backend unit tests and TypeScript build
cd backend/functions
npm ci --no-audit --no-fund
npm run check
cd ../..

# Confirm Firebase DEV deployed function, no deployment
firebase functions:list --project siraba-dev

# Emulator rules tests (first install test deps inside tests/firebase)
./scripts/test-firebase.sh

# Inspect changes before commit; never commit temporary diagnostics or credentials
git status --short
git diff --check
```

### Pending SB-P1-014 positive callable test

Download `apply_siraba_backend_positive_test_014_fixed.py` into `C:/Users/USER/Downloads` first. Use **only fixed variant**:

```bash
cd /c/2026/SiraBa/sira-ba
python "C:/Users/USER/Downloads/apply_siraba_backend_positive_test_014_fixed.py" --build-install
# On phone: enter dedicated DEV test user credentials; share only PASS/FAIL code.
# After test, always remove temporary code:
python "C:/Users/USER/Downloads/apply_siraba_backend_positive_test_014_fixed.py" --remove
cd android-app
./gradlew :app:testDevDebugUnitTest :app:connectedDevDebugAndroidTest :app:installDevDebug --no-daemon
cd ..
git status --short
```

Never commit local Firebase JSON, debug App Check token, password, local logs, or temporary test activity. Never use broad `git add .` without reviewing changes. Never include `.git` directories in shared source ZIPs.

## 9. Suggested first HOA outreach message

> Hello! I'm piloting **Sira Ba?**, a local service for small household repairs such as leaking pipes and clogged sinks. I'm looking for one nearby subdivision to try a limited, opt-in pilot with a few screened local repair workers. Residents would receive clear labor/material quotes and pay workers directly; Sira Ba? would charge no booking fee during the pilot. Could I schedule a short conversation to see whether this would be useful for your community? Any resident communication or worker entry arrangements would follow your HOA's rules and approval.

## 10. Next meeting / next chat checklist

1. Confirm this approved strategy is versioned in GitHub under `docs/HOA_PILOT_STRATEGY.md`; keep blueprint untouched.
2. Finish SB-P1-014 authenticated DEV callable positive test, clean up patch, rerun Android tests.
3. Complete remaining Phase 1 evidence and security gates before declaring Phase 1 accepted.
4. Create shortlist of 3 nearby HOAs, choose first HOA, request permission; identify 3–5 worker candidates.
5. Build a simple privacy-conscious, no-ad lead tracker (no unnecessary personally identifiable data) and define inquiry/worker operating rules.
6. Use actual evidence—not nationwide worker counts or app installs—to decide whether to expand.
