package com.jonasgerdes.stoppelmap.usecase.main.presenter

import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainInteractor
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState
import com.jonasgerdes.stoppelmap.util.plusAssign
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class MainPresenter(private val view: MainView, interactor: MainInteractor) : Disposable {
    val disposable = CompositeDisposable()

    init {
        disposable += interactor.state
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::render)

        disposable += view.getNavigationEvents()
                .map { it.itemId }
                .distinctUntilChanged()
                .debounce(100, TimeUnit.MILLISECONDS)
                .subscribe(interactor::onNavigationClicked)

        disposable += view.getIntents()
                .subscribe(interactor::onIntentReceived)
    }

    private fun render(state: MainViewState) {
        view.showView(state)
        view.selectNavigation(state.selectedItemId)
    }

    override fun isDisposed(): Boolean {
        return disposable.isDisposed
    }

    override fun dispose() {
        disposable.dispose()
    }
}