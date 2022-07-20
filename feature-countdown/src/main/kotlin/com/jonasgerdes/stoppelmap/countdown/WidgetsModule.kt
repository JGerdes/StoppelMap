package com.jonasgerdes.stoppelmap.countdown

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.jonasgerdes.stoppelmap.countdown.repository.WidgetSettingsRepository
import com.jonasgerdes.stoppelmap.countdown.usecase.CalculateGingerbreadHeartColorsFromHSLUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadWidgetSettings
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadWidgetSettingsViewModel
import com.jonasgerdes.stoppelmap.countdown.widget.skyline.SkylineWidgetSettings
import com.jonasgerdes.stoppelmap.countdown.widget.skyline.SkylineWidgetSettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val SHARED_PREFERENCES_NAME = "prefs_widgets"

val countdownModule = module {

    factory { get<Context>().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE) }
    factory { GetOpeningCountDownFlowUseCase(getOpeningCountDown = get()) }
    factory { GetOpeningCountDownUseCase() }

    factory(named("GingerbreadWidgetSettings")) {
        WidgetSettingsRepository(
            sharedPreferences = get(),
            loadFromPreferences = GingerbreadWidgetSettings::loadFromPreferences,
            saveToPreferences = GingerbreadWidgetSettings::saveToPreferences
        )
    }
    factory(named("SkylineWidgetSettings")) {
        WidgetSettingsRepository(
            sharedPreferences = get(),
            loadFromPreferences = SkylineWidgetSettings::loadFromPreferences,
            saveToPreferences = SkylineWidgetSettings::saveToPreferences
        )
    }
    factory { CalculateGingerbreadHeartColorsFromHSLUseCase() }

    viewModel {
        GingerbreadWidgetSettingsViewModel(
            widgetSettingsRepository = get(named("GingerbreadWidgetSettings")),
            calculateGingerbreadHeartColorsFromHSL = get()
        )
    }
    viewModel {
        SkylineWidgetSettingsViewModel(
            widgetSettingsRepository = get(named("SkylineWidgetSettings")),
        )
    }
}
