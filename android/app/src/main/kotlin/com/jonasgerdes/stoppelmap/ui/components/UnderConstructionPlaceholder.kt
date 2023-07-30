package com.jonasgerdes.stoppelmap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.compose.*
import com.jonasgerdes.stoppelmap.R

@Composable
fun UnderConstructionPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.Asset("animation/under_construction.json"))
        val animationColor = MaterialTheme.colorScheme.onBackground.toArgb()
        val dynamicProperties = rememberLottieDynamicProperties(
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR, value = animationColor, keyPath = arrayOf(
                    "Wheel",
                    "Car1",
                    "Car1",
                    "Fill",
                )
            ),
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR, value = animationColor, keyPath = arrayOf(
                    "Wheel",
                    "Car2",
                    "Car2",
                    "Fill",
                )
            ),
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR, value = animationColor, keyPath = arrayOf(
                    "Wheel",
                    "Wheel",
                    "Wheel",
                    "Fill",
                )
            ),
            rememberLottieDynamicProperty(
                property = LottieProperty.COLOR, value = animationColor, keyPath = arrayOf(
                    "Base",
                    "Base",
                    "Base",
                    "Fill",
                )
            ),
        )
        LottieAnimation(
            composition = composition,
            dynamicProperties = dynamicProperties,
            iterations = LottieConstants.IterateForever,
            alignment = Alignment.Center,
            modifier = Modifier.size(192.dp)
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(id = R.string.main_available_soon),
            style = MaterialTheme.typography.displaySmall
        )
    }
}
