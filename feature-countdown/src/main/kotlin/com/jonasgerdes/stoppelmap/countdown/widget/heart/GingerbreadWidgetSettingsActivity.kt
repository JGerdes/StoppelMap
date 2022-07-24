@file:OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLifecycleComposeApi::class
)

package com.jonasgerdes.stoppelmap.countdown.widget.heart

import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.countdown.R
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class GingerbreadWidgetSettingsActivity : ComponentActivity() {

    private val sharedPreferences: SharedPreferences by inject()
    private val viewModel: GingerbreadWidgetSettingsViewModel by viewModel()

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        viewModel.onAppWidgetIdChanged(
            appWidgetId = intent.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )!!
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onAppWidgetIdChanged(
            appWidgetId = intent.extras?.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
            )!!
        )
        setContent {
            StoppelMapTheme {
                val state by viewModel.state.collectAsStateWithLifecycle()

                (state as? GingerbreadWidgetSettingsViewModel.ViewState.Done)?.run {
                    saveWidgetAndFinish(settings)
                }
                Scaffold(
                    topBar = {
                        CenterAlignedTopAppBar(
                            title = { Text(text = stringResource(id = R.string.widget_configuration_title)) },
                        )
                    }
                ) { scaffoldPadding ->
                    GingerbreadWidgetSettingsScreen(
                        state = state,
                        onShowHoursChange = viewModel::onShowHoursChanged,
                        onHueChange = viewModel::onHueChanged,
                        onSaturationChange = viewModel::onSaturationChanged,
                        onBrightnessChange = viewModel::onBrightnessChanged,
                        onColorSelect = viewModel::onColorSelected,
                        onSaveTap = viewModel::onSaveSettingsTapped,
                        modifier = Modifier
                            .padding(scaffoldPadding)
                            .fillMaxSize()
                    )
                }
            }
        }
    }

    private fun saveWidgetAndFinish(settings: GingerbreadWidgetSettings) {
        val views = GingerbreadHeartWidgetProvider().initWidget(
            context = this,
            settings = settings
        )

        val appWidgetManager = AppWidgetManager.getInstance(baseContext)
        appWidgetManager.updateAppWidget(settings.appWidgetId, views)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, settings.appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

}
