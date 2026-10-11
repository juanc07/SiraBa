package net.thinkbloxph.siraba

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.key
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import net.thinkbloxph.siraba.presentation.MainViewModel
import net.thinkbloxph.siraba.ui.navigation.SiraBaNavHost
import net.thinkbloxph.siraba.ui.theme.SiraBaTheme

class MainActivity : ComponentActivity() {
    // SB-P1-006 preview decision: re-display brand intro when returning from background.
    // Revisit during Phase 2 external authentication flows, which must not be interrupted.
    private var appOpenGeneration by mutableIntStateOf(0)

    private val model: MainViewModel by viewModels {
        MainViewModel.Factory((application as SiraBaApplication).container.authRepository)
    }

    override fun onRestart() {
        super.onRestart()
        appOpenGeneration += 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val session by model.state.collectAsStateWithLifecycle()
            SiraBaTheme {
                key(appOpenGeneration) {
                    SiraBaNavHost(state = session)
                }
            }
        }
    }
}
