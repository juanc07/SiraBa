package net.thinkbloxph.siraba.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import net.thinkbloxph.siraba.data.auth.AuthRepository
import net.thinkbloxph.siraba.data.auth.AuthStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface SessionUiState {
    data object Loading : SessionUiState
    data class Ready(val auth: AuthStatus) : SessionUiState
}

class MainViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val mutableState = MutableStateFlow<SessionUiState>(SessionUiState.Loading)
    val state: StateFlow<SessionUiState> = mutableState.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                authRepository.observeSession().collect { auth ->
                    mutableState.value = SessionUiState.Ready(auth)
                }
            } catch (_: Exception) {
                mutableState.value = SessionUiState.Ready(
                    AuthStatus.Failure("Sign-in status unavailable. Try again later.")
                )
            }
        }
    }

    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = MainViewModel(repository) as T
    }
}
