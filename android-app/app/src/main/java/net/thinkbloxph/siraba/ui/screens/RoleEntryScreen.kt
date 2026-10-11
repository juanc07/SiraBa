package net.thinkbloxph.siraba.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.thinkbloxph.siraba.ui.components.SiraBaHeader
import net.thinkbloxph.siraba.ui.testing.UiTestTags

/** Role entry is a preview, not a live signup or a promise of eligible workers. */
@Composable
fun RoleEntryScreen(isWorker: Boolean, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize().testTag(
        if (isWorker) UiTestTags.WORKER_SCREEN else UiTestTags.CUSTOMER_SCREEN
    )) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter).widthIn(max = 560.dp).fillMaxWidth()
                .verticalScroll(rememberScrollState()).padding(horizontal = 22.dp, vertical = 20.dp)
        ) {
            SiraBaHeader()
            Spacer(Modifier.height(20.dp))
            TextButton(onClick = onBack, modifier = Modifier.testTag(UiTestTags.BACK_TO_WELCOME)) {
                Text("←  Bumalik", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(Modifier.height(34.dp))
            Text(
                if (isWorker) "PARA SA LOCAL WORKERS" else "PARA SA MGA BAHAY",
                color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(12.dp))
            Text(
                if (isWorker) "Mas malapit na trabaho.\nMas klarong usapan."
                else "Mas simple ang\npagpapaayos.",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(15.dp))
            Text(
                if (isWorker) "Mag-apply, magpa-verify, at tumanggap ng angkop na maliliit na repair job sa inyong lugar."
                else "Mag-post ng litrato, tingnan ang itemized quote, at pumili ng angkop na verified worker.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(28.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Surface(shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer) {
                        Text("COMING SOON", modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            style = MaterialTheme.typography.labelMedium)
                    }
                    Text(if (isWorker) "Worker onboarding" else "Customer onboarding",
                        style = MaterialTheme.typography.titleLarge)
                    Text("Inihahanda pa namin ang secure na account at booking experience.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant)
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = .3f))
                    StepLine("01", if (isWorker) "Gumawa ng account" else "Gumawa ng account")
                    StepLine("02", if (isWorker) "Isumite ang verification requirements" else "Ilarawan at kunan ng litrato ang sira")
                    StepLine("03", if (isWorker) "Tumanggap ng kwalipikadong request" else "Ihambing ang malinaw na quotes")
                }
            }
            Spacer(Modifier.height(22.dp))
            Text("Wala pang totoong bookings o approved worker profiles sa preview build na ito.",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun StepLine(index: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Surface(shape = MaterialTheme.shapes.small, color = MaterialTheme.colorScheme.primaryContainer) {
            Text(index, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium)
        }
        Text(label, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium)
    }
}
