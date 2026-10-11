# Reuse-vs-rebuild inventory

| Component | Source reference | Decision | Rationale and test guard |
|---|---|---|---|
| Gradle wrapper | `gradlew*`, `gradle/wrapper/*` | REUSE generic scripts/JAR only | Generic build bootstrap; re-pin and validate hash/provenance |
| Dependency version catalog | `gradle/libs.versions.toml` | ADAPT | Keep viable versions, remove game/Solana dependencies; external resolution still unverified |
| Compose theme/screen patterns | `ui/theme`, `ui/screens`, `ui/components` | REBUILD | Sira Ba? Filipino accessibility/brand, do not copy Radiant art or behavior |
| UI state + test tags | `ui/testing/UiTestTags.kt` | REIMPLEMENT | Stable Compose control tags and explicit Loading/Ready/Error patterns |
| Pure domain rules and tests | `logic/*Rules.kt`, `app/src/test` | REIMPLEMENT | Repair safety/worker eligibility/quote states are different; pure Kotlin tested |
| Firebase Auth / Google identity | `auth/GoogleAccountCredentialProvider.kt` | LATER ADAPT | Needs new OAuth, account-linking, identity safety; Phase 2 |
| Firebase repository boundary | `firebase/FirebaseRadiantRepository.kt` | REIMPLEMENT SPLIT | Original 4,807-line service too coupled; isolated AuthRepository foundation |
| Approximate area | `location/ApproximateLocationProvider.kt` | LATER ADAPT | Barangay privacy design requires new consent and geographic policies |
| Security rules | `firebase/firestore.rules` | REBUILD FROM DENY-ALL | Only service-specific data/ownership checks may enable access |
| Functions callable pattern | `functions/index.js` | REIMPLEMENT | Auth/App Check guard, bounded costs, Node 22; no game/AI features |
| Emulator infrastructure | `firebase.json` | REBUILD | New auth/firestore/storage/functions ports and deny-negative tests |
| CI Android | `.github/workflows/android-ci.yml` | EXPAND | Add Kotlin, Node/policy, emulator rules, source guard, Android assemble |
| Operational documents | `docs/operating-system/` | REWRITE FOR PRODUCT | Concise status, decision, test, handoff logs |
| Solana/ORE/SKR, game, social/leaderboards, image/audio assets | throughout | EXCLUDE | Irrelevant or risky imported product assumptions |
| Existing Firebase app IDs, auth data, OAuth client IDs, keys, scripts | project-specific | EXCLUDE | Must create own project/credentials and no live data import |

**No Kotlin/JS gameplay or Radiant-specific source code copied.** Generic Gradle wrapper is the only directly reused build asset; the rest are independent implementation and patterns.

## Future reuse policy

Only promote a component into a shared library after Sira Ba? succeeds and a second product demonstrates compatible semantics. Each future reuse must document original license, dependencies, data authority, security-review notes, and a passing test relevant to both apps.
