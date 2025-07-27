package com.jonasgerdes.stoppelmap.schedule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jonasgerdes.stoppelmap.schedule.ui.ScheduleScreen
import kotlinx.serialization.Serializable


@Serializable
data object ScheduleDestination


fun NavGraphBuilder.scheduleDestinations() {
    composable<ScheduleDestination> {
        ScheduleScreen(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}