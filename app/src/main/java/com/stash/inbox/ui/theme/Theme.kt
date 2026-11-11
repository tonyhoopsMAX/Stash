package com.stash.inbox.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

val PureBlack = Color(0xFF000000)
val NeonAccent = Color(0xFF00FF9D)
val TextPrimary = Color.White
val TextMuted = Color(0x99FFFFFF)

private val StashColors = darkColorScheme(
    primary = NeonAccent,
    onPrimary = PureBlack,
    background = PureBlack,
    onBackground = TextPrimary,
    surface = PureBlack,
    onSurface = TextPrimary
)

@Composable
fun StashTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = StashColors, content = content)
}

/** Translucent gradients and a directional highlight, rather than a blur over black. */
fun Modifier.obsidianGlass(shape: Shape = RoundedCornerShape(24.dp)): Modifier =
    clip(shape)
        .background(
            Brush.verticalGradient(listOf(Color(0x1FFFFFFF), Color(0x0AFFFFFF))),
            shape
        )
        .border(
            1.dp,
            Brush.linearGradient(
                listOf(Color(0x4DFFFFFF), Color(0x05FFFFFF), Color(0x1A00FF9D)),
                start = Offset.Zero,
                end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
            ),
            shape
        )

@Composable
fun AmbientLight(modifier: Modifier = Modifier, centered: Boolean = false, alpha: Float = 0.08f) {
    Canvas(modifier.fillMaxSize()) {
        val center = if (centered) Offset(size.width / 2, size.height / 2)
        else Offset(size.width * 0.85f, size.height * 0.15f)
        val radius = size.width * 0.7f
        drawCircle(
            brush = Brush.radialGradient(
                listOf(NeonAccent.copy(alpha = alpha), Color.Transparent), center, radius
            ),
            radius = radius,
            center = center
        )
    }
}

@Composable
fun GlassIconButton(icon: ImageVector, description: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier.size(48.dp).obsidianGlass(CircleShape).clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = description, tint = TextPrimary, modifier = Modifier.size(20.dp))
    }
}
