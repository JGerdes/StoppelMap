package com.jonasgerdes.stoppelmap.usecase.main.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.usecase.main.presenter.MainPresenter
import com.jonasgerdes.stoppelmap.usecase.main.presenter.MainView
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainInteractor
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState
import com.jonasgerdes.stoppelmap.usecase.map.view.MapFragment
import com.jonasgerdes.stoppelmap.util.enableItemShifting
import com.jonasgerdes.stoppelmap.util.enableItemTextHiding
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class MainActivity : AppCompatActivity(), MainView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.enableItemShifting(false)
        navigation.enableItemTextHiding(true)

        val interactor = ViewModelProviders.of(this).get(MainInteractor::class.java)
        MainPresenter(this, interactor)
    }

    private fun showFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    override fun showView(state: MainViewState) {
        when (state) {
            is MainViewState.Map -> showFragment(MapFragment())
        }
    }

    override fun selectBottomNavigation(selectedItemId: Int) {
        navigation.selectedItemId = selectedItemId
    }

}
