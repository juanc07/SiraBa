package net.thinkbloxph.siraba.di

import net.thinkbloxph.siraba.data.auth.AuthRepository
import net.thinkbloxph.siraba.data.auth.FirebaseAuthRepository
import net.thinkbloxph.siraba.firebase.FirebaseErrorReporter

/** Explicit constructor-injection boundary; no DI framework required for MVP. */
class AppContainer(firebaseConfigured: Boolean) {
    val errorReporter = FirebaseErrorReporter(firebaseConfigured)
    val authRepository: AuthRepository = FirebaseAuthRepository(firebaseConfigured, errorReporter)
}
