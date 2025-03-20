package com.example.kalagatotask.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.kalagatotask.ui.theme.Pink80
import kotlin.math.hypot

@Composable
fun CircularRevealLayout(
    modifier: Modifier = Modifier,
    onAnimationEnd: () -> Unit = {}
) {
    var radius by remember { mutableFloatStateOf(0f) }
    val animatedRadius = remember { Animatable(0f) }

    val (width, height) = with(LocalConfiguration.current) {
        with(LocalDensity.current) { screenWidthDp.dp.toPx() to screenHeightDp.dp.toPx() }
    }

    val maxRadiusPx = hypot(width, height)

    LaunchedEffect(animatedRadius) {
        animatedRadius.animateTo(
            maxRadiusPx,
            animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing)
        ) {
            radius = value
        }
        onAnimationEnd()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .drawBehind {
                drawCircle(
                    color = Pink80,
                    radius = radius,
                    center = Offset(size.width, size.height)
                )
            },
        contentAlignment = Alignment.Center
    ) {
    }
}