package com.rahul.grocer.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rahul.grocer.ui.theme.NebulaPurple
import com.rahul.grocer.ui.theme.StarlightSilver

@Composable
fun OrbitLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    isAnimated: Boolean = false
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbit_transition")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbit_rotation"
    )

    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "orbit_pulse"
    )

    val currentRotation = if (isAnimated) rotation else 0f
    val currentPulse = if (isAnimated) pulse else 1f

    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2, this.size.height / 2)
        val radius = (this.size.width / 2) * 0.8f

        // Outer Orbit Ring
        rotate(currentRotation, center) {
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        NebulaPurple,
                        StarlightSilver.copy(alpha = 0.5f),
                        Color.Transparent,
                        NebulaPurple
                    )
                ),
                radius = radius,
                center = center,
                style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        // Inner Planet (Pulsing)
        drawCircle(
            color = NebulaPurple,
            radius = (radius * 0.4f) * currentPulse,
            center = center
        )
        
        // Satellite
        rotate(-currentRotation * 1.5f, center) {
             drawCircle(
                color = StarlightSilver,
                radius = radius * 0.15f,
                center = center + Offset(radius * 0.7f, 0f)
            )
        }
    }
}
