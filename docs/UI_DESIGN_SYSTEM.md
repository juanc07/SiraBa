# SB-P1-004 — Sira Ba? mobile UI system

Status: **Source implemented; Android compilation/device visual QA UNVERIFIED in this environment.**

## Why this change exists
The phase-1 proof-of-life screens were essentially a plain column of text/buttons. The app now has a coherent, mobile-first visual foundation suitable for a Filipino micro-repair service, without claiming to have implemented marketplace features.

## Product look and feel
- Voice: Filipino-first, clear, grounded, useful rather than luxury or generic home-contractor claims.
- Primary brand: Deep navy/ink `#122D38`; teal `#087F81` / `#065E63`; warm paper `#F8F8F4`; mint `#E4F4EF`; gold `#FFD48D` accent.
- Typography: system font with strong headline hierarchy; no additional bundled font asset or licensing exposure.
- Components: Material 3 shapes/buttons/surfaces; 62 dp primary/secondary actions; scrolling and max-width content; system dark/light schemes.
- Imagery: Compose vector-style house/repair motifs for the foundation. Real problem/worker photos must only be shown when users supply authorized images under approved security rules.
- Brand: `SIRA BA?` and `Picture-an. Presyuhan. Paayos.` always visible on onboarding.
- Trust: No fake worker profiles, verification badges, ratings, service availability, or bookings; all screen roles remain previews.

## Screen ownership
- `ui/theme/Theme.kt`: colors, typography, shape tokens and system dark/light schemes.
- `ui/components/BrandElements.kt`: local reusable brand mark, hero, process steps, header.
- `ui/screens/WelcomeScreen.kt`: hero, value proposition, two real navigation actions, honest non-operational notice.
- `ui/screens/RoleEntryScreen.kt`: role-specific explanation / preview, back action; no sign-in or write operations.
- `ui/navigation/SiraBaNavHost.kt`: preserves single source of route authority, provides back navigation.
- `ui/testing/UiTestTags.kt`, `androidTest/FoundationNavigationTest.kt`: stable welcome/role tags and back test.

## UX QA checklist (required, not yet completed)
- [ ] On a real Android device: launch without crash; touch targets and text are visible.
- [ ] Customer entry navigates; back returns; worker entry navigates; back returns.
- [ ] Test with small-width handset (320–360 dp), increased font settings, and portrait/landscape.
- [ ] Dark mode: maintain contrast and readable text; never rely on color alone.
- [ ] TalkBack: reading order, content labels, navigation and preview notice.
- [ ] Dynamic content: long Filipino wording scrolls without clipping.
- [ ] Auth state: developer diagnostic remains optional, doesn't expose PII or give fake verification.
- [ ] Regression: `:app:testDevDebugUnitTest`, `:app:assembleDevDebug`, `:app:connectedDevDebugAndroidTest` and `:app:installDevDebug`.

## Do not confuse with Phase 2
This patch creates no sign-in, provider verification, request posting, quote, booking, or payment capability. Firestore/Storage rules, Cloud Functions and the payment feature gate remain unchanged.

## Source and decisions
Master Blueprint v1.0 specifies Filipino-first, low-cost phone UI, honest verification and booking-fee statements. Android Kotlin Firebase Operating System specifies phone-first Compose, state-down/events-up and testing tags. Visual implementation uses stable Compose Material 3 APIs without forcing dependency upgrades. Review current official Android Material 3 guidance before later UI changes.

## SB-P1-006 — Visual-reference alignment and always-show intro
- Visual reference: `Sira Ba_ App Design Preview.png` supplied by product owner. The previous image was a **concept mockup**, not an Android screenshot. This patch replaces the simplified first two Compose screens with a closer implementation.
- Intro uses a warm pale sky, prominent generated brand logo, locally bundled illustrative repair worker, and teal Magsimula CTA. The artwork is derived from the approved concept preview; it is not a worker identity or transaction claim.
- Welcome displays two real tappable role cards with illustrated customer/worker portraits, step icons and truthful pilot/preview disclosures. Artwork is stored locally as `drawable-nodpi` resources; no new network dependency or permissions.
- Decision changed: **show the intro on every fresh app process/launch**, not only on the first install. `IntroPreferences` historical storage is ignored; existing users require no app-data reset. Pressing Magsimula replaces intro with welcome until next fresh app launch.
- Returning from background invokes `MainActivity.onRestart`, resetting the intro navigation (preview-only policy). Fresh launches also start at intro. Activity recreation for rotation can retain the current route. This must be revisited before Phase 2 external sign-in/permission flows.
- Preview images approximate the supplied mockup; they are not a pixel-perfect recreation. Role screens remain explicit non-functional previews until Phase 2.
- Android `:app:testDevDebugUnitTest`, `:app:assembleDevDebug`, `:app:installDevDebug`, `:app:connectedDevDebugAndroidTest`, and real Samsung visual QA remain unverified after this change.
