package net.thinkbloxph.siraba.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.thinkbloxph.siraba.R
import net.thinkbloxph.siraba.ui.theme.SiraBaColors

@Composable
fun SiraBaMark(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.siraba_logo_mark),
        contentDescription = "Sira Ba? icon",
        modifier = modifier.size(46.dp)
    )
}

@Composable
fun SiraBaHeader(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SiraBaMark(Modifier.size(46.dp))
        Column(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f)
        ) {
            Text(
                "Sira Ba?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Picture-an. Presyuhan. Paayos.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Surface(shape = RoundedCornerShape(50), color = MaterialTheme.colorScheme.primaryContainer) {
            Text(
                "PREVIEW",
                modifier = Modifier.padding(horizontal = 11.dp, vertical = 7.dp),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Composable
fun RepairHero(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth().height(194.dp)
        .background(
            brush = Brush.linearGradient(listOf(Color(0xFF0D5D62), Color(0xFF12333F))),
            shape = RoundedCornerShape(28.dp)
        )
    ) {
        Canvas(Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            drawCircle(Color(0xFF268C88).copy(alpha = .42f), w * .41f, Offset(w * .87f, h * .24f))
            drawCircle(Color(0xFFB1E6CD).copy(alpha = .12f), w * .30f, Offset(w * .90f, h * .91f))
            val x = w * .79f
            val y = h * .49f
            val white = Color.White.copy(alpha = .88f)
            val line = 4.dp.toPx()
            drawLine(white, Offset(x - w * .14f, y), Offset(x, y - h * .25f), line, cap = StrokeCap.Round)
            drawLine(white, Offset(x, y - h * .25f), Offset(x + w * .14f, y), line, cap = StrokeCap.Round)
            drawRoundRect(white.copy(alpha = .13f), topLeft = Offset(x - w * .112f, y),
                size = androidx.compose.ui.geometry.Size(w * .224f, h * .28f),
                cornerRadius = CornerRadius(9.dp.toPx()))
            drawLine(white, Offset(x - w * .11f, y), Offset(x - w * .11f, y + h * .25f), line, cap = StrokeCap.Round)
            drawLine(white, Offset(x + w * .11f, y), Offset(x + w * .11f, y + h * .25f), line, cap = StrokeCap.Round)
            drawCircle(SiraBaColors.Gold, radius = 12.dp.toPx(), center = Offset(x, y + h * .12f))
            drawCircle(white, radius = 15.dp.toPx(), center = Offset(x, y + h * .12f), style = Stroke(width = 2.dp.toPx()))
        }
        Column(
            modifier = Modifier.align(Alignment.CenterStart).padding(start = 22.dp, end = 12.dp),
            verticalArrangement = Arrangement.spacedBy(9.dp)
        ) {
            Surface(color = Color.White.copy(alpha = .12f), shape = RoundedCornerShape(30.dp)) {
                Text("MALAPIT. MALINAW.", modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp),
                    color = Color(0xFFD4FAEC), style = MaterialTheme.typography.labelMedium)
            }
            Text("Maliit na sira.\nMas madaling ayos.", color = Color.White,
                style = MaterialTheme.typography.titleLarge)
            Text("Libreng booking sa pilot", color = SiraBaColors.Gold,
                style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
fun HowItWorks(modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Ganito kasimple", style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground)
        FlowStep("01", "Picture-an", "Kunan ng litrato ang maliit na sira.")
        FlowStep("02", "Presyuhan", "Ihambing ang malinaw na labor at materyales.")
        FlowStep("03", "Paayos", "Pumili ng kwalipikadong worker kapag available na.")
    }
}

@Composable
private fun FlowStep(number: String, title: String, detail: String) {
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        Box(
            modifier = Modifier.size(42.dp).background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(number, color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelLarge)
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground)
            Text(detail, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
