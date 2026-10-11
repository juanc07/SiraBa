# Sira Ba? — architecture baseline

## Repo layout

```text
sira-ba/
├── android-app/
│   ├── gradle/libs.versions.toml
│   ├── app/src/main/java/net/thinkbloxph/siraba/
│   │   ├── ui/{screens,components,navigation,theme,testing}/
│   │   ├── presentation/           # ViewModel, explicit session state
│   │   ├── domain/{model,rules}/   # pure deterministic Kotlin
│   │   ├── data/auth/              # repository interface + Firebase implementation
│   │   ├── firebase/               # App Check/emulator/error reporting
│   │   └── di/                     # explicit composition root
│   └── app/src/{debug,release,test,androidTest}/
├── backend/functions/src/           # Gen 2 typed read-only callable + free policy
├── firebase/                        # zero-trust firestore + storage + indexes
├── tests/{firebase,kotlin}/         # integration-negative / standalone tests
├── docs/                             # authoritative handoff
├── scripts/                          # verification commands
└── .github/workflows/
```

Single Gradle `:app` module deliberately chosen for a low-capital Phase 1 MVP. Use clear packages now, introduce Android feature/Gradle modules only if compile time, ownership or reuse justifies them; separate backend modules are already present.

## Component ownership

| Layer | Owns | Forbidden |
|---|---|---|
| `ui/` Compose/Navigation | Render mobile screen, button clicks, accessibility | Firestore mutations, privilege decisions |
| `presentation/` ViewModel | lifecycle-aware Flow collection, observable state | Authority for worker verified, payment states |
| `domain/` | pure categorization and eventually quote/transition invariants | Firebase SDK, Android Context, credentials |
| `data/` | repository contracts and Firebase integration adapters | rendering, client-authoritative verification |
| trusted `backend/functions/` | authenticated commands, server transactions, role and state guards | trusting user-supplied fees or role strings |
| `firebase/*.rules` | deny/grant least-privileged client read access | treating Admin SDK as rule-constrained |

### Current launch path

`MainActivity → Session ViewModel → AuthRepository → FirebaseAuth (if configured)`.
Compose Navigation renders Welcome -> Customer entry / Worker entry. These are **not** live repair or onboarding flows; all entry screens explicitly say functionality is not available yet.

### Firebase bootstrap

- DEV variant application ID `net.thinkbloxph.siraba.dev`; staging `net.thinkbloxph.siraba.staging`; PROD `net.thinkbloxph.siraba` (**provisional IDs**).
- `google-services.json` local under each `app/src/<flavor>/` directory. Applying Google Services plugin is conditional on at least one config file, so a blank-source build remains possible. Never copy the Radiant file.
- In DEV debug only, optional `-PuseFirebaseEmulators=true` routes Auth, Firestore, Singapore-region Functions, Storage to `10.0.2.2` ports. Run the emulator CLI with the **same project ID as DEV `google-services.json`**; use independent `demo-siraba` only for rules tests. `src/devDebug/` permits HTTP to that emulator bridge only; all other builds reject cleartext by default.
- Firebase App Check: debug provider in debug source-set; Play Integrity provider in release source-set. Enforcement/test token registration still unverified.
- Crashlytics default collection OFF until lawful privacy review and consent/notice handling; sanitized internal diagnostic code only. FCM SDK dependency installed but no token collection or worker push flows yet.

### Marketplace command architecture (later phases)

```text
Customer UI -> ViewModel -> Repository -> HTTPS Callable Function
                                      -> authenticate + App Check
                                      -> server fetch role and verification status
                                      -> validate category, quote, owner, revision
                                      -> Firestore transaction (state + event + idempotency)
                                      -> Firestore read models -> UI Flow
```

No full address is stored in publicly readable request document. Future `requestPrivate/{requestId}` / scoped accepted-booking secrets need their own design, retention/PII controls and access tests. Worker verification evidence must be in restricted Storage namespaces and never readable by workers outside an approved relationship. Worker approval cannot be self-asserted.

### Payment invariant

`backend/functions/src/billingPolicy.ts` is the single currently executable fee contract: mode `DISABLED`, rate proposal `500` bps, fee amount `0` centavos, settlement `NOT_APPLICABLE`; no gateway SDK, billing writes, credits or force-enable endpoint. Future server-side versioned policy requires dual approvals, separate legal and provider sign-off, and explicit Phase 8/12 changes. `getFoundationStatus` is read-only, requires auth and App Check, and rejects caller-supplied fields.

### Risks/open design

- Android version-catalog versions and actual Studio toolchain resolution **not yet compiled in this runtime**.
- Selected future Firestore schema, query indexes and role grants will be designed alongside each feature; currently all client access denied.
- Remote DEV/STAGING/PROD Firebase projects and secrets have not been created or linked.
- The full ID KYC, privacy retention, phone OTP and exact-address policy are open; don't build data collection before agreement.
