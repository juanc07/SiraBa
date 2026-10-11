package net.thinkbloxph.siraba.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.thinkbloxph.siraba.presentation.SessionUiState
import net.thinkbloxph.siraba.ui.screens.IntroScreen
import net.thinkbloxph.siraba.ui.screens.RoleEntryScreen
import net.thinkbloxph.siraba.ui.screens.WelcomeScreen

private object Routes {
    const val INTRO = "intro"
    const val WELCOME = "welcome"
    const val CUSTOMER = "customer"
    const val WORKER = "worker"
}

@Composable
fun SiraBaNavHost(state: SessionUiState) {
    val nav = rememberNavController()
    Scaffold { padding ->
        NavHost(
            navController = nav,
            // Product decision SB-P1-006: show the branded intro on every fresh launch.
            // Never read the historical first-run preference as route authority.
            startDestination = Routes.INTRO,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.INTRO) {
                IntroScreen(onStart = {
                    nav.navigate(Routes.WELCOME) {
                        popUpTo(Routes.INTRO) { inclusive = true }
                        launchSingleTop = true
                    }
                })
            }
            composable(Routes.WELCOME) {
                WelcomeScreen(
                    state,
                    onCustomer = { nav.navigate(Routes.CUSTOMER) },
                    onWorker = { nav.navigate(Routes.WORKER) }
                )
            }
            composable(Routes.CUSTOMER) {
                RoleEntryScreen(isWorker = false, onBack = { nav.popBackStack() })
            }
            composable(Routes.WORKER) {
                RoleEntryScreen(isWorker = true, onBack = { nav.popBackStack() })
            }
        }
    }
}
