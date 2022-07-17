package com.jonasgerdes.stoppelmap.countdown

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import org.koin.dsl.module

private const val SHARED_PREFERENCES_NAME = "prefs_widgets"

val countdownModule = module {

    factory { get<Context>().getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE) }
    factory { GetOpeningCountDownFlowUseCase(getOpeningCountDown = get()) }
    factory { GetOpeningCountDownUseCase() }
}
