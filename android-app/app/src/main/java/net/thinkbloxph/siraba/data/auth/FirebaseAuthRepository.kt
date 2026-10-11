package net.thinkbloxph.siraba.data.auth

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOf

class FirebaseAuthRepository(
    private val firebaseConfigured: Boolean,
    private val reporter: net.thinkbloxph.siraba.firebase.ErrorReporter
) : AuthRepository {
    override fun observeSession(): Flow<AuthStatus> {
        if (!firebaseConfigured) return flowOf(AuthStatus.Unconfigured)
        return callbackFlow {
            val auth = try { FirebaseAuth.getInstance() } catch (e: Exception) {
                reporter.report("firebase_auth_init", e)
                trySend(AuthStatus.Failure("Firebase Auth could not initialize"))
                close()
                return@callbackFlow
            }
            val listener = FirebaseAuth.AuthStateListener { instance ->
                val uid = instance.currentUser?.uid
                trySend(if (uid == null) AuthStatus.SignedOut else AuthStatus.SignedIn(uid))
            }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }
    }
}
