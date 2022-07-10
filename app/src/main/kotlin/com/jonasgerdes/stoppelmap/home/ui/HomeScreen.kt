@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)

package com.jonasgerdes.stoppelmap.home.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.home.ui.components.CountdownCard

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.home_topbar_title)) })
        LazyColumn(
            contentPadding = PaddingValues(16.dp), modifier = Modifier.fillMaxSize()
        ) {
            item {
                CountdownCard(days = 30, hours = 12, minutes = 4)
            }
        }
    }
}
