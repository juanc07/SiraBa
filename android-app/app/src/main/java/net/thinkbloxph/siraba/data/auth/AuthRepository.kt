package net.thinkbloxph.siraba.data.auth

import kotlinx.coroutines.flow.Flow

sealed interface AuthStatus {
    data object Unconfigured : AuthStatus
    data object SignedOut : AuthStatus
    data class SignedIn(val uid: String) : AuthStatus
    data class Failure(val message: String) : AuthStatus
}

interface AuthRepository { fun observeSession(): Flow<AuthStatus> }
