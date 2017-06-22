package com.jonasgerdes.stoppelmap.usecase.main.presenter

import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainInteractor
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class MainPresenter(private val view: MainView, interactor: MainInteractor) {
    init {
        interactor.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)

        view.getNavigationEvents()
                .map { it.itemId }
                .debounce(100, TimeUnit.MILLISECONDS)
                .distinctUntilChanged()
                .subscribe(interactor::onNavigationClicked)
    }

    private fun render(state: MainViewState) {
        view.showView(state)
        view.selectNavigation(state.selectedItemId)
    }
}