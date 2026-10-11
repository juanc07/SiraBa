package net.thinkbloxph.siraba

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.crashlytics.FirebaseCrashlytics
import net.thinkbloxph.siraba.di.AppContainer
import net.thinkbloxph.siraba.firebase.configureFirebaseForBuild

/** Does not silently sign in, create users, or write production data. */
class SiraBaApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val firebase = FirebaseApp.initializeApp(this)
        if (firebase != null) {
            configureFirebaseForBuild(this)
            // No automatic crash uploads before a reviewed notice/consent policy.
            FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(false)
        }
        container = AppContainer(firebase != null)
    }
}
