package com.example.ui.sand

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.example.data.model.ReelTheme

/**
 * Ultra-Lightweight, Emulator-Safe Kinetic Sand Table Canvas.
 * Only animates when isRunning is true; completely static when paused or in gallery previews.
 */
@Composable
fun FullScreenSandTable(
    theme: ReelTheme,
    progress: Float,
    isRunning: Boolean,
    isFullScreen: Boolean = true,
    lightingMode: SandLightingMode = SandLightingMode.GALLERY_WHITE,
    modifier: Modifier = Modifier
) {
    val sandEngine = remember { KineticSandEngine() }

    val animTick = if (isRunning) {
        val infiniteTransition = rememberInfiniteTransition(label = "sand_ball_roll")
        val tick by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "ball_roll_tick"
        )
        tick
    } else {
        0f
    }

    val smoothProgress = if (isRunning) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress.coerceIn(0f, 1f),
            animationSpec = tween(
                durationMillis = 1000,
                easing = LinearEasing
            ),
            label = "smooth_sand_progress"
        )
        animatedProgress
    } else {
        progress.coerceIn(0f, 1f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("full_screen_sand_table")
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            sandEngine.drawRealisticSandTable(
                drawScope = this,
                theme = theme,
                progress = smoothProgress,
                frameNanos = (animTick * 10000000L).toLong(),
                isFullScreen = isFullScreen,
                lightingMode = lightingMode
            )
        }
    }
}
