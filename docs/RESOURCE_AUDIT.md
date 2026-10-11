# Phase 0 — Resource audit

Date: 2026-10-11 (Asia/Manila). Scope: static resource review only, not a passed Android build or security penetration test. **Original four resources left unchanged**.

## Files examined and authority

| Resource | SHA256 prefix | Review result | Authority |
|---|---|---|---|
| `Sira_Ba_Master_Project_Blueprint_v1_0.docx` | `b9c3d5b43622b493c8` | Parsed 385 paragraphs, 28 tables; read major sections, appendices and data contracts | Binding product requirements |
| `Sira_Ba_Master_Project_Blueprint_v1_0.pdf` | `420ca37fd97f7a0ce7` | 23-page PDF extracted and checked against section structure of DOCX | Same blueprint in PDF presentation |
| `Android_Kotlin_Firebase_Operating_System_From_Radiant_Circle.docx` | `7744fd6fa7bd4094c6` | Parsed 272 paragraphs, 19 tables; reviewed 25-section operating procedure | Engineering guidance, subordinate to blueprint |
| `RadiantCircle-Final-Reference-20261011-4b55cb0.zip` | `263aff4b27786ea2d96fbd7edaf1d623584c449c3fd875ce2e7072528ced86f0` | Extracted read-only into temporary external audit area; reviewed manifest, Android/Gradle, Firebase, backend, CI and key docs | Pattern library only |

**Important:** SHA prefix is only a quick identity indicator; compare full SHA256 for conclusive equivalence. The DOCX and PDF were compared for key requirements and structure, **not certified pixel-for-pixel equal**. The unprovided older Reality Platform Master Plan is not treated as reviewed.

## A. Blueprint requirement extraction

- Purpose: photo-first, hyperlocal, budget-sensitive minor plumbing/clog/fixture repair; no renovations/general cleaning/major dangerous work initially.
- Journeys: customer posts photo/category/barangay; nearby active VERIFIED worker itemizes labor, materials and travel; customer selects a quote; worker accepts; precise address unlocks only then; before/after evidence, customer confirmation and disputes.
- Roles: customer, applicant/verified worker, verifier/admin, possible future finance role. Authentication is **not** equivalent to trade verification.
- Trusted backend must own worker approvals, quote/version locks, booking transitions, private address releases, dispute resolution and any monetization.
- Billing: initial `paymentMode=DISABLED`, `paymentsEnabled=false`, `feeRateBps=500` retained **only as proposed future rate**; actual platform fee=0, worker paid directly, historical FREE bookings never retrocharged.
- Business: pilot territory not fixed (Cavite candidate); QA and operational acceptance gates required before public beta. FREE pilot may still have legal/privacy/registration requirements.
- Product roadmap: Phase 0–12, specifically Phase 1 repository/environment, Phase 2 identity/roles, Phase 3 verification, Phase 4 request, Phase 5 quotes, Phase 6 booking, Phase 7 completion/disputes, Phase 8 fee readiness, Phase 12 activation only after separate approvals.

## B. Operating-system extraction

- Newest accepted source + git status/commit check before changes; trace UI/domain/data/trusted authority; make one small patch with acceptance and failure tests.
- Compose state down/events up, phone-first UI, stable UI test tags and explicit loading/error/offline states.
- Pure Kotlin rules with deterministic unit tests; Firebase rules as source; Cloud Functions for privileged changes.
- Gradle test/assemble, connected emulator, manual device checks, source inspection, small diffs, frozen release/handoff docs, never report unexecuted tests as PASS.
- Reference ZIP should stay immutable outside the new app. No speculative Reality Core extraction until a second product needs it.

## C. What the ZIP ACTUALLY contains

Static extraction found **349 ZIP entries** including **63 production Kotlin source files**, **29 Kotlin unit-test files**, **1 Android instrumentation-test file**, extensive documentation, and Gradle wrapper scripts/JAR. Important verified samples:

| Actual path in reference | Finding |
|---|---|
| `gradle/libs.versions.toml` | AGP 9.0.1, Kotlin Compose plugin 2.4.20, Compose BoM 2026.04.01, Firebase BoM 34.6.0, Gradle wrapper 9.3.0 |
| `app/build.gradle.kts` | Single `:app`, compile/target SDK 36, min SDK 26; Firebase Auth/Firestore/Functions plus wallet/Solana deps |
| `app/src/main/java/.../ui/RadiantRushApp.kt` | Large Compose shell using UI state and route handling; not a plug-and-play navigation module |
| `app/src/main/java/.../ui/screens/` + `ui/components/` + `ui/theme/` | Reusable composition/typography/design patterns; screen content is app-specific |
| `app/src/main/java/.../logic/*Rules.kt` | Pure deterministic rules isolated from Android SDK, reusable **approach**, not portable domain behavior |
| `app/src/main/java/.../firebase/FirebaseRadiantRepository.kt` | Central Auth/Firestore repository of **4,807 lines**—use central boundary idea but avoid recreating monolith |
| `app/src/main/java/.../auth/GoogleAccountCredentialProvider.kt` | Google sign-in/account linking precedent; new OAuth client IDs required |
| `app/src/main/java/.../location/ApproximateLocationProvider.kt` | Coarse location precedent; new consent/privacy policy required |
| `firebase/firestore.rules` | Detailed product-specific rules spanning **741 lines**, with some client-write permissions; **unsafe to copy** for Sira Ba? |
| `firebase.json` | Firestore emulator/rules structure; no reusable Sira Ba? environment settings |
| `functions/index.js` / `functions/package.json` | Node 22, Gen 2 HTTPS callable pattern, Firebase Admin and OpenAI dependencies; not a marketplace backend |
| `.github/workflows/android-ci.yml` | Checkout, JDK 17, Gradle unit test + debug APK; missing Firebase emulator and secret checks |
| `docs/operating-system/`, `docs/development-history/` | Source traceability, UI, test/release and handoff lessons; don't copy product-specific history |

**Reuse constraints:** reference app has no evidence of Hilt/Koin DI or a full feature-module design, and a custom/manual Compose navigator appears in its shell. Do **not** claim its implementations already supply Sira Ba?'s navigation, repository interfaces, robust Firebase Emulator security suite, or payment architecture.

## D. Dependency/compatibility assessment

- Kept a small subset of reference's AGP/Kotlin/Compose/Firebase versions, added Navigation Compose and lifecycle state collection; **resolution/build remains UNVERIFIED** without Android SDK and access to Maven/Gradle.
- AGP 9 uses built-in Kotlin; do **not** add obsolete `org.jetbrains.kotlin.android` plugin. Kotlin Compose compiler plugin remains separate.
- Firebase BoM 34.x: depend on Firebase main modules, not removed/deprecated `*-ktx` Firebase artifacts.
- Firebase Admin SDK and Functions use Node 22; added TypeScript command scaffold and a pure policy test runnable without npm dependencies.
- App Check, Crashlytics, Storage and FCM are **new integrations** added as foundations, not found as demonstrated working SDK integrations in the reference's app/build configuration.
- Avoid Solana mobile wallet adapter, web3, OpenAI/Radiant prompts, game audio, crypto ledgers/leaderboards/quests, and generic Reality Core dependency.

## E. Secrets and security inspection

- ZIP listing contained no exact tracked `google-services.json`, `.env`, keystore, service-account JSON or private key files. It contains a placeholder `app/google-services.json.example` with Radiant-specific old package text.
- Regex search of extracted text found **no obvious embedded** Google API keys, OpenAI `sk-` keys, token prefixes or PEM private-key headers. **This is not forensic proof of zero secrets**; rotated/obfuscated/unusual encodings may evade scans.
- Reference Firestore rule privileges are unsuitable for new booking state: notably client write patterns for Radiant user/game data. Not adopted.
- No LICENSE found at top levels of reference ZIP; therefore no product source is transplanted. Only generic Gradle wrapper scripts/JAR were reused as build tooling; license/redistribution provenance should be confirmed before publishing.
- Sira Ba? initial rules deny all client Firestore and Storage reads/writes; no privileged commands write data; customer app cannot turn paid mode on. Admin SDK bypasses Firestore security rules, so future backend handlers require independent authorization.

## F. Differences / reconciliations

1. **Monetization:** operating-system example §18 mentions an older worker-job-credit model. The binding blueprint supersedes it: **future customer-paid 5%**, currently FREE and DISABLED. No worker-credit code or architecture.
2. **Phase 1 authentication:** OS generic phase 1 says app installs + signs in; Sira Ba? blueprint puts full signup in Phase 2. Phase 1 implements Auth boundary and explicit unavailable/signed-out states; production-grade login is Phase 2. Kickoff's stricter Phase 1 definition-of-done still requires real DEV config and verified app launch.
3. **Legacy Firebase access:** reference's app-level persistence and permissions must not be copied; Sira Ba? begins strict deny-all with future reads/writes enabled only alongside tested ownership controls.
4. **Future price engine:** fee basis rate is documented but no charge path is written; Phase 8 creates richer versioned policy after regulatory/product sign-off, Phase 12 is commercial activation only.

## G. Audit outcome

**Phase 0 technical resource audit: COMPLETE.** Product-level Phase 0 *scope lock* has open decisions (first pilot area, minimum job economics, worker KYC and support staffing), so production-readiness is not implied. See `DECISION_LOG.md` and `PHASE_STATUS.md`.
