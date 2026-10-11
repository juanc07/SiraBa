package net.thinkbloxph.siraba.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.thinkbloxph.siraba.R
import net.thinkbloxph.siraba.ui.testing.UiTestTags

private val Ink = Color(0xFF102A45)
private val Teal = Color(0xFF079C9B)

/** Branded app entry shown on every fresh app launch. */
@Composable
fun IntroScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFFDDF4FE), Color.White, Color(0xFFF8F7F1))))
            .testTag(UiTestTags.INTRO_SCREEN)
    ) {
        Column(
            modifier = Modifier.align(Alignment.TopCenter)
                .widthIn(max = 510.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.siraba_logo_mark),
                contentDescription = "Sira Ba? icon",
                modifier = Modifier.width(144.dp).height(144.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "Sira Ba?",
                color = Ink,
                fontSize = 28.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(4.dp))
            Text(
                "Picture-an. Presyuhan. Paayos.",
                style = MaterialTheme.typography.titleMedium,
                color = Ink,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(18.dp))
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(296.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Brush.verticalGradient(listOf(Color(0xFFE6F8FC), Color(0xFFFFF5E8))))
            ) {
                Image(
                    painter = painterResource(R.drawable.siraba_intro_worker),
                    contentDescription = null,
                    modifier = Modifier.align(Alignment.BottomEnd)
                        .width(208.dp).height(295.dp),
                    contentScale = ContentScale.Fit
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart)
                        .width(190.dp)
                        .padding(start = 16.dp, bottom = 22.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        "Maliit na sira.\nMalaking ginhawa.",
                        color = Ink,
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        "Picture-an ang problema at humingi ng malinaw na presyo mula sa local worker.",
                        color = Color(0xFF304C59),
                        fontSize = 12.sp,
                        lineHeight = 17.sp
                    )
                }
            }
            Spacer(Modifier.height(22.dp))
            Button(
                onClick = onStart,
                modifier = Modifier.fillMaxWidth().height(60.dp).testTag(UiTestTags.INTRO_START),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal, contentColor = Color.White)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text("Magsimula", fontSize = 19.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(12.dp))
                    Text("→", fontSize = 24.sp)
                }
            }
            Spacer(Modifier.height(13.dp))
            Text(
                "Preview pa lamang • Wala pang aktibong booking",
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xFF526676),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}
