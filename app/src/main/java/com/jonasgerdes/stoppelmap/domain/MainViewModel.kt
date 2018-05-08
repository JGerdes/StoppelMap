package com.jonasgerdes.stoppelmap.domain

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import com.jonasgerdes.mvi.Flow
import com.jonasgerdes.mvi.interpret
import com.jonasgerdes.mvi.process

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainViewModel : ViewModel() {

    val events = PublishRelay.create<MainEvent>()
    val state by lazy { flow.start(events) }

    val flow = Flow<MainEvent, MainState>(interpret {
        when (it) {

        }
    }, process(

    ),
            MainReducer(),
            { Log.d("Flow", it) }
    )
}