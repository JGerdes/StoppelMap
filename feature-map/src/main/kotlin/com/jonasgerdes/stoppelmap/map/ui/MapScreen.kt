package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MapScreen(modifier: Modifier = Modifier) {

    Text(text = "Hello Map", textAlign = TextAlign.Center, modifier = modifier)
}
