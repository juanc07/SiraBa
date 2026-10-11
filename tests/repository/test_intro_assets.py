"""Source invariants for always-visible branded intro and visual reference assets."""
from pathlib import Path
import unittest
import xml.etree.ElementTree as ET

ROOT = Path(__file__).resolve().parents[2]
MAIN = ROOT / "android-app/app/src/main"
SOURCE = MAIN / "java/net/thinkbloxph/siraba"

class IntroBrandingSourceTests(unittest.TestCase):
    def test_intro_is_default_every_launch(self):
        nav = (SOURCE / "ui/navigation/SiraBaNavHost.kt").read_text(encoding="utf-8")
        self.assertIn('startDestination = Routes.INTRO', nav)
        self.assertNotIn('hasSeenIntro', nav)
        self.assertNotIn('onFinishIntro()', nav)
        self.assertIn('inclusive = true', nav)

    def test_intro_button_and_preview_notice(self):
        intro = (SOURCE / "ui/screens/IntroScreen.kt").read_text(encoding="utf-8")
        for expected in ("Magsimula", "siraba_logo_mark", "Sira Ba?", "siraba_intro_worker", "UiTestTags.INTRO_START", "Wala pang aktibong booking"):
            self.assertIn(expected, intro)

    def test_main_activity_does_not_skip_intro(self):
        main = (SOURCE / "MainActivity.kt").read_text(encoding="utf-8")
        self.assertNotIn("IntroPreferences", main)
        self.assertIn("SiraBaNavHost(state = session)", main)
        self.assertIn("override fun onRestart()", main)
        self.assertIn("key(appOpenGeneration)", main)

    def test_portraits_are_real_png_assets(self):
        drawable = MAIN / "res/drawable-nodpi"
        for name in ("siraba_brand_logo.png", "siraba_launcher_foreground.png", "siraba_logo_mark.png", "siraba_intro_worker.png", "siraba_customer_portrait.png", "siraba_worker_portrait.png"):
            self.assertTrue((drawable / name).read_bytes().startswith(b'\x89PNG\r\n\x1a\n'), name)

    def test_manifest_launcher_resource_is_retained(self):
        app = ET.parse(MAIN / 'AndroidManifest.xml').getroot().find('application')
        self.assertEqual(app.attrib.get('{http://schemas.android.com/apk/res/android}icon'), '@mipmap/ic_launcher')
        self.assertTrue((MAIN / 'res/mipmap-anydpi-v26/ic_launcher.xml').is_file())

    def test_customer_worker_cards_have_real_navigation(self):
        welcome = (SOURCE / 'ui/screens/WelcomeScreen.kt').read_text(encoding='utf-8')
        for expected in ('onClick = onCustomer', 'onClick = onWorker', 'siraba_customer_portrait', 'siraba_worker_portrait', 'UiTestTags.CUSTOMER_ENTRY', 'UiTestTags.WORKER_ENTRY'):
            self.assertIn(expected, welcome)

if __name__ == '__main__':
    unittest.main()
