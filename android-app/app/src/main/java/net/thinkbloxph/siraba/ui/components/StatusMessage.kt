package net.thinkbloxph.siraba.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import net.thinkbloxph.siraba.data.auth.AuthStatus
import net.thinkbloxph.siraba.presentation.SessionUiState

@Composable
fun SessionIndicator(state: SessionUiState) {
    when (state) {
        SessionUiState.Loading -> CircularProgressIndicator()
        is SessionUiState.Ready -> Column {
            val msg = when (state.auth) {
                AuthStatus.Unconfigured -> "Offline preview: connect Firebase DEV to enable sign-in."
                AuthStatus.SignedOut -> "Firebase initialized locally. Sign-in opens in Phase 2."
                is AuthStatus.SignedIn -> "Authenticated session found. Profile and roles come in Phase 2."
                is AuthStatus.Failure -> state.auth.message
            }
            Text(msg, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp))
        }
    }
}
