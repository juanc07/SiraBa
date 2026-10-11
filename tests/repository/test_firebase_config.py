"""Offline tests for environment isolation; do not include real Firebase identifiers."""
import json
import tempfile
import unittest
from pathlib import Path
from scripts.validate_firebase_config import validate_all, validate_one


def write_config(root, env, project, package):
    path = root / 'android-app' / 'app' / 'src' / env / 'google-services.json'
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps({
        'project_info': {'project_id': project, 'project_number': '1234567890'},
        'client': [{'client_info': {'android_client_info': {'package_name': package},
                                    'mobilesdk_app_id': '1:1234567890:android:abcdef012345'},
                    'api_key': [{'current_key': 'fake-test-key-not-real'}]}]
    }), encoding='utf-8')
    return path


class ConfigValidatorTest(unittest.TestCase):
    def test_preview_has_no_config(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            self.assertEqual(validate_all(root), [])
            self.assertEqual(len(validate_all(root, require_dev=True)), 1)

    def test_valid_dev_file(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            write_config(root, 'dev', 'siraba-test-dev', 'net.thinkbloxph.siraba.dev')
            self.assertEqual(validate_all(root, require_dev=True), [])

    def test_wrong_package(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            write_config(root, 'dev', 'siraba-test-dev', 'ph.not-siraba.app')
            self.assertEqual(len(validate_all(root, require_dev=True)), 1)

    def test_duplicate_firebase_project_for_flavors_rejected(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            write_config(root, 'dev', 'siraba-same-project', 'net.thinkbloxph.siraba.dev')
            write_config(root, 'prod', 'siraba-same-project', 'net.thinkbloxph.siraba')
            self.assertTrue(any('shares' in e for e in validate_all(root)))

    def test_incomplete_stub_rejected(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            path = write_config(root, 'dev', 'siraba-dev', 'net.thinkbloxph.siraba.dev')
            data = json.loads(path.read_text(encoding='utf-8'))
            del data['client'][0]['client_info']['mobilesdk_app_id']
            path.write_text(json.dumps(data), encoding='utf-8')
            with self.assertRaises(ValueError):
                validate_one(path, 'net.thinkbloxph.siraba.dev')

    def test_radiant_reference_id_rejected(self):
        with tempfile.TemporaryDirectory() as d:
            root = Path(d)
            config = write_config(root, 'dev', 'radiant-old-project', 'net.thinkbloxph.siraba.dev')
            with self.assertRaises(ValueError):
                validate_one(config, 'net.thinkbloxph.siraba.dev')


if __name__ == '__main__':
    unittest.main()
