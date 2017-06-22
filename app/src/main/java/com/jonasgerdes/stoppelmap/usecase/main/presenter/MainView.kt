package com.jonasgerdes.stoppelmap.usecase.main.presenter

import android.support.annotation.IdRes
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
interface MainView {
    fun showView(state: MainViewState)
    fun selectBottomNavigation(@IdRes selectedItemId: Int)
}