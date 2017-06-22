package com.jonasgerdes.stoppelmap.usecase.main.viewmodel

import android.arch.lifecycle.ViewModel
import io.reactivex.subjects.BehaviorSubject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class MainInteractor : ViewModel() {

    private val stateSubject = BehaviorSubject.createDefault(MainViewState.Map())

    val state get() = stateSubject.hide()
}