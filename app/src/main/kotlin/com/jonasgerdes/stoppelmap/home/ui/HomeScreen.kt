@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalComposeUiApi::class,
    ExperimentalLifecycleComposeApi::class
)

package com.jonasgerdes.stoppelmap.home.ui

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_MUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.countdown.ui.components.CountdownCard
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadHeartWidgetProvider
import com.jonasgerdes.stoppelmap.countdown.widget.heart.WidgetSettingsActivity
import org.koin.androidx.compose.viewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<HomeViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(
        modifier = modifier
    ) {
        CenterAlignedTopAppBar(title = { Text(text = stringResource(id = R.string.home_topbar_title)) })
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            when (val countDownState = state.openingCountDownState) {
                is HomeViewModel.CountDownState.CountingDown -> item {
                    CountdownCard(
                        days = countDownState.daysLeft,
                        hours = countDownState.hoursLeft,
                        minutes = countDownState.minutesLeft
                    )
                }
                HomeViewModel.CountDownState.Loading -> Unit
                HomeViewModel.CountDownState.Over -> Unit
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                && AppWidgetManager.getInstance(context).getAppWidgetIds(
                    ComponentName(
                        "com.jonasgerdes.stoppelmap",
                        "com.jonasgerdes.stoppelmap.widgets.GingerbreadHeartWidgetProvider"
                    )
                ).isEmpty()
            ) {
                item {
                    Card {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = "Homescreen Widgets",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Button(onClick = { addWidget(context) }) {
                                Text(text = "Jetzt hinzufügen")
                            }
                        }
                    }
                }
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
private fun addWidget(context: Context) {
    val appWidgetManager = AppWidgetManager.getInstance(context)
    val myProvider = ComponentName(context, GingerbreadHeartWidgetProvider::class.java)

    if (appWidgetManager.isRequestPinAppWidgetSupported) {
        val successCallback: PendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                Intent(context, WidgetSettingsActivity::class.java),
                FLAG_UPDATE_CURRENT.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it or FLAG_MUTABLE
                    } else it
                }
            )
        appWidgetManager.requestPinAppWidget(myProvider, null, successCallback)
    }
}
