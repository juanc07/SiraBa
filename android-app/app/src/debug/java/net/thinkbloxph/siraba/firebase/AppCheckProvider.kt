package net.thinkbloxph.siraba.firebase

import com.google.firebase.appcheck.FirebaseAppCheck
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory

// Debug providers are NOT shipped in release source sets.
fun installAppCheckProvider() {
    FirebaseAppCheck.getInstance().installAppCheckProviderFactory(
        DebugAppCheckProviderFactory.getInstance()
    )
}
