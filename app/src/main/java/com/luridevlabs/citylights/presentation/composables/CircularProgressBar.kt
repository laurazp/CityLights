package com.luridevlabs.citylights.presentation.composables

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(){
    val progressValue = 0.75f
    val infiniteTransition = rememberInfiniteTransition(label = "progressBar")

    val progressAnimationValue by infiniteTransition.animateFloat(
        initialValue = 0.0f,
        targetValue = progressValue,animationSpec = infiniteRepeatable(animation = tween(900)),
        label = "progressBar"
    )
    Box(modifier = Modifier
        .fillMaxWidth(),
        contentAlignment = Alignment.Center
        ) {
        CircularProgressIndicator(
            modifier = Modifier
                .width(30.dp)
                .height(30.dp),
            progress = progressAnimationValue)
    }
}