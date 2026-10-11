package net.thinkbloxph.siraba.firebase

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.storage.FirebaseStorage
import net.thinkbloxph.siraba.BuildConfig

/** App Check is installed by source-set-specific implementations. */
fun configureFirebaseForBuild(context: Context) {
    installAppCheckProvider()
    if (BuildConfig.USE_FIREBASE_EMULATORS) {
        check(BuildConfig.DEBUG && BuildConfig.ENVIRONMENT == "DEV") {
            "Firebase emulators are strictly forbidden outside DEV debug"
        }
        FirebaseAuth.getInstance().useEmulator("10.0.2.2", 9099)
        FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080)
        FirebaseFunctions.getInstance("asia-southeast1").useEmulator("10.0.2.2", 5001)
        FirebaseStorage.getInstance().useEmulator("10.0.2.2", 9199)
    }
}
