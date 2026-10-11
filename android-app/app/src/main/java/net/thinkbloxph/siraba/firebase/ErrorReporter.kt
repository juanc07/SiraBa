package net.thinkbloxph.siraba.firebase

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics

/** Redact PII before calling; this boundary is not a general user-data logger. */
interface ErrorReporter { fun report(code: String, error: Throwable) }

class FirebaseErrorReporter(private val firebaseConfigured: Boolean) : ErrorReporter {
    override fun report(code: String, error: Throwable) {
        // No Throwable messages in Logcat by default: Firebase errors can include identifiers.
        Log.e("SiraBa", "Internal event: $code")
        if (firebaseConfigured) {
            // Crashlytics collection is disabled until privacy/compliance sign-off.
            FirebaseCrashlytics.getInstance().recordException(
                IllegalStateException("SiraBa internal diagnostic: $code")
            )
        }
    }
}
