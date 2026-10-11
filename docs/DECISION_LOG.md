# Decision log (append-only after acceptance)

| ID | Date | Decision | Why | Status |
|---|---|---|---|---|
| ADR-001 | 2026-10-11 | Keep all four resources immutable outside repository | Source authority, traceability, no legacy contamination | ACCEPTED |
| ADR-002 | 2026-10-11 | Adopt monorepo `android-app/backend/firebase/docs/tests` | Keeps one feature vertical slice coherent; avoids unnecessary Android modules | ACCEPTED |
| ADR-003 | 2026-10-11 | Use pure Kotlin rules and a single Gradle `:app` | Small, independently testable foundation | ACCEPTED |
| ADR-004 | 2026-10-11 | All client Firestore and Storage access initially DENIED | Never accidentally expose PII or allow forgery | ACCEPTED; emulator verification pending |
| ADR-005 | 2026-10-11 | Fee contract `DISABLED`; proposed future rate=500 bps, actual fee=0 | Approved free booking, no legacy worker credits | ACCEPTED; backend policy tests pass |
| ADR-006 | 2026-10-11 | Authentication boundary in Phase 1, real sign-in/roles Phase 2 | Align blueprint detailed phases with kickoff foundation scope | ACCEPTED |
| ADR-007 | 2026-10-11 | Conditional Google services config, distinct product flavors | Build UI preview without leaking/guessing project IDs | PROPOSED until Android build |
| ADR-008 | 2026-10-11 | New package base `ph.siraba.app` | Independent Sira Ba? Android identity | PROVISIONAL; verify availability/owner |
| ADR-009 | 2026-10-11 | Do not use Hilt/Koin yet | Constructor-injection boundary enough in Phase 1 | ACCEPTED |
| ADR-010 | 2026-10-11 | No live Firebase deployment or production customer data from this environment | Remote auth/project access absent | ACCEPTED |
| ADR-011 | 2026-10-11 | Force LF Unix Gradle script via `.gitattributes`; preserve Windows `.bat` | Initial bootstrap ZIP's `gradlew` CRLF was broken on Linux/CI | FIX APPLIED, script launch tested |
| ADR-012 | 2026-10-11 | Release build explicitly disables emulator routing; do not reject DEV debug during Gradle variant configuration | Original `beforeVariants` check would block the intended `-PuseFirebaseEmulators=true` DEV debug build | FIX APPLIED, Gradle build unverified |
| ADR-013 | 2026-10-11 | Validate local Firebase JSON application IDs and distinct project IDs before build | Prevent accidental cross-environment project reuse without exposing JSON | UNIT TESTED, live projects unverified |

## Blueprint open questions retained (do not invent answers)

- D-01: First pilot barangay/city and service radius: Cavite candidate, **not confirmed**.
- D-02: Worker minimum labor/travel economics and cancellation terms.
- D-03: Phone, KYC/ID-document and consent policy proportionality.
- D-04: Multiple-itemized quotes are the working default.
- D-05: Approved labor snapshot basis for revisions; policy unresolved.
- D-06: Credits vs exact-fee collection: **on hold**. Neither implemented.
- D-07: Support SLA, remedies/return-work policy not guaranteed.
- D-08: Optional simple marketing site deferred.
- D-09: Staffing/support hours unresolved.
- D-10: Legacy worker-credit model **superseded** by future customer-paid proposed 5% booking fee.

## Unresolved engineering choices

- Resolve full Android SDK/Maven dependencies; pin/lock packages, resolve Gradle warnings, verify debug/release App Check and Crashlytics behavior.
- Decide post-Phase-1 worker-verification admin tool and IAM model before ID documents are collected.
- Define public/private Firestore projection schema and collection retention before enabling any client reads.
- Verify company-name/Android package ownership before public distribution; no live payments until documented legal/commercial gate.


### D-SB-014 — Portable, evidence-first Phase 1 validation (2026-10-11)
Decision: provide a Python command runner so Windows developers can reproduce basic
checks without executing several platform-dependent shell scripts. Command logs and
result JSON stay local and gitignored, since device output may include identifiers.
Explicit limit: automated local PASS cannot substitute for cloud authentication,
Firebase emulator security tests, a real Android run, or successful GitHub CI.

| ADR-015 | 2026-10-11 | Set Android namespace and production application ID to `net.thinkbloxph.siraba` (DEV `.dev`, STAGING `.staging`); supersedes provisional ADR-008 | Align Sira Ba? app identity to intended ThinkBlox PH domain `thinkbloxph.net`; no Firebase app or project is created or renamed by this source patch | ACCEPTED, Android/Firebase runtime unverified |

| ADR-017 | 2026-10-11 | First-run branded Compose intro with logo and persistent local completed flag | Separate brand intro from role selection; launcher mark is teal house + orange wrench; no backend authority | IMPLEMENTED SOURCE; device validation pending |

| ADR-018 | 2026-10-11 | Honor visual mockup for intro and role selection; show intro on fresh launch and app return from background | User explicitly prefers a repeated brand entry with Magsimula, not an automatic return to role selection; the prior image was only conceptual | SOURCE IMPLEMENTED; on-device validation pending |
