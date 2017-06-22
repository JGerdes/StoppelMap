package com.jonasgerdes.stoppelmap.usecase.main.presenter

import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainInteractor
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class MainPresenter(private val view: MainView, interactor: MainInteractor) {
    init {
        interactor.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)
    }

    private fun render(state: MainViewState) {
        view.showView(state)
        view.selectBottomNavigation(state.selectedItemId)
    }
}