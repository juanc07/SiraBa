package net.thinkbloxph.siraba.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.thinkbloxph.siraba.R
import net.thinkbloxph.siraba.presentation.SessionUiState
import net.thinkbloxph.siraba.ui.components.SessionIndicator
import net.thinkbloxph.siraba.ui.components.SiraBaHeader
import net.thinkbloxph.siraba.ui.testing.UiTestTags

private val Ink = Color(0xFF102A45)
private val Teal = Color(0xFF079C9B)
private val Orange = Color(0xFFFFA719)

/** Interactive role selection based on the approved design mockup. Preview only; no fake bookings. */
@Composable
fun WelcomeScreen(state: SessionUiState, onCustomer: () -> Unit, onWorker: () -> Unit) {
    Box(Modifier.fillMaxSize().background(Color.White).testTag(UiTestTags.WELCOME_SCREEN)) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter)
                .widthIn(max = 510.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            SiraBaHeader()
            Spacer(Modifier.height(28.dp))
            Text(
                "Ano ang\nhinahanap mo?",
                fontSize = 34.sp,
                lineHeight = 38.sp,
                letterSpacing = (-0.8).sp,
                fontWeight = FontWeight.ExtraBold,
                color = Ink
            )
            Spacer(Modifier.height(7.dp))
            Text("Piliin ang role para magpatuloy.", fontSize = 16.sp, color = Color(0xFF526170))
            Spacer(Modifier.height(23.dp))
            RoleChoiceCard(
                title = "Customer",
                description = "May ipapaayos sa bahay? Magsimula rito.",
                illustration = R.drawable.siraba_customer_portrait,
                isCustomer = true,
                onClick = onCustomer,
                tag = UiTestTags.CUSTOMER_ENTRY
            )
            Spacer(Modifier.height(13.dp))
            RoleChoiceCard(
                title = "Worker",
                description = "Mag-apply bilang local repair worker.",
                illustration = R.drawable.siraba_worker_portrait,
                isCustomer = false,
                onClick = onWorker,
                tag = UiTestTags.WORKER_ENTRY
            )
            Spacer(Modifier.height(25.dp))
            Text("Paano gumagana?", fontSize = 19.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
            Spacer(Modifier.height(15.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                ProcessPreviewStep("▣", "1", "Picture-an\nang sira", Modifier.weight(1f))
                ProcessPreviewStep("₱", "2", "Kumuha ng\npresyo", Modifier.weight(1f))
                ProcessPreviewStep("✓", "3", "Paayos sa\nlocal worker", Modifier.weight(1f))
            }
            Spacer(Modifier.height(23.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                InfoPill("✓", "Worker checks", "Bago ma-verify", Modifier.weight(1f), Teal)
                InfoPill("₱", "Klarong presyo", "Labor at materyales", Modifier.weight(1f), Orange)
            }
            Spacer(Modifier.height(17.dp))
            Text(
                "PREVIEW: Wala pang live bookings o approved worker profiles. Libre ang booking kapag inilunsad ang pilot.",
                fontSize = 12.sp,
                lineHeight = 18.sp,
                color = Color(0xFF526170)
            )
            var diagnosticsVisible by remember { mutableStateOf(false) }
            TextButton(onClick = { diagnosticsVisible = !diagnosticsVisible }) {
                Text(if (diagnosticsVisible) "Itago ang developer status" else "Developer status")
            }
            if (diagnosticsVisible) SessionIndicator(state)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
private fun RoleChoiceCard(
    title: String,
    description: String,
    illustration: Int,
    isCustomer: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    val gradient = if (isCustomer) {
        listOf(Color(0xFFE4F5F2), Color(0xFFF0FAF9))
    } else {
        listOf(Color(0xFFFFF0D9), Color(0xFFFFF8EA))
    }
    val accent = if (isCustomer) Teal else Orange
    Row(
        modifier = Modifier.fillMaxWidth().height(146.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Brush.horizontalGradient(gradient))
            .clickable(role = Role.Button, onClick = onClick)
            .testTag(tag)
            .semantics { contentDescription = "$title: $description" },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(illustration),
            contentDescription = null,
            modifier = Modifier.width(128.dp).fillMaxHeight(),
            contentScale = ContentScale.Crop
        )
        Column(modifier = Modifier.weight(1f).padding(start = 3.dp, end = 1.dp)) {
            Text(title, fontSize = 22.sp, lineHeight = 26.sp, fontWeight = FontWeight.ExtraBold, color = Ink)
            Spacer(Modifier.height(5.dp))
            Text(description, fontSize = 12.5.sp, lineHeight = 17.sp, color = Color(0xFF394D5C))
        }
        Box(
            Modifier.padding(end = 12.dp).size(33.dp).background(accent, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text("→", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun ProcessPreviewStep(symbol: String, number: String, caption: String, modifier: Modifier) {
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(58.dp).background(Color(0xFFE8F7F5), CircleShape), contentAlignment = Alignment.Center) {
            Text(symbol, color = Teal, fontSize = 27.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(5.dp))
        Box(Modifier.size(21.dp).background(Color(0xFFB9EBE5), CircleShape), contentAlignment = Alignment.Center) {
            Text(number, fontSize = 11.sp, color = Ink, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(6.dp))
        Text(caption, fontSize = 12.sp, lineHeight = 16.sp, textAlign = TextAlign.Center, color = Ink)
    }
}

@Composable
private fun InfoPill(symbol: String, title: String, subtitle: String, modifier: Modifier, accent: Color) {
    Surface(modifier, shape = RoundedCornerShape(17.dp), color = Color(0xFFF5F8F7)) {
        Row(Modifier.padding(horizontal = 9.dp, vertical = 12.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.size(29.dp).background(accent, CircleShape), contentAlignment = Alignment.Center) {
                Text(symbol, color = Color.White, fontSize = 17.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(7.dp))
            Column {
                Text(title, fontSize = 11.5.sp, lineHeight = 15.sp, fontWeight = FontWeight.Bold, color = Ink)
                Text(subtitle, fontSize = 9.5.sp, lineHeight = 13.sp, color = Color(0xFF526170))
            }
        }
    }
}
