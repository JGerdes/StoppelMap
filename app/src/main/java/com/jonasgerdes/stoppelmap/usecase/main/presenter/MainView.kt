package com.jonasgerdes.stoppelmap.usecase.main.presenter

import android.net.Uri
import android.support.annotation.IdRes
import android.view.MenuItem
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState
import io.reactivex.Observable

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
interface MainView {
    fun showView(state: MainViewState)
    fun selectNavigation(@IdRes selectedItemId: Int)

    fun getNavigationEvents(): Observable<MenuItem>
    fun getIntents(): Observable<Uri>
}