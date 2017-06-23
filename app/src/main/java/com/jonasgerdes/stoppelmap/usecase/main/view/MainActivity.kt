package com.jonasgerdes.stoppelmap.usecase.main.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jakewharton.rxbinding2.support.design.widget.itemSelections
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.usecase.event.overview.view.EventOverviewFragment
import com.jonasgerdes.stoppelmap.usecase.information.view.InformationFragment
import com.jonasgerdes.stoppelmap.usecase.main.presenter.MainPresenter
import com.jonasgerdes.stoppelmap.usecase.main.presenter.MainView
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainInteractor
import com.jonasgerdes.stoppelmap.usecase.main.viewmodel.MainViewState
import com.jonasgerdes.stoppelmap.usecase.map.view.MapFragment
import com.jonasgerdes.stoppelmap.usecase.transportation.overview.view.TransportOverviewFragment
import com.jonasgerdes.stoppelmap.util.enableItemShifting
import com.jonasgerdes.stoppelmap.util.enableItemTextHiding
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class MainActivity : AppCompatActivity(), MainView {

    val mapFragment = MapFragment()
    val eventScheduleFragment = EventOverviewFragment()
    val busScheduleFragment = TransportOverviewFragment()
    val informationFragment = InformationFragment()
    var currentFragment = BehaviorSubject.create<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.enableItemShifting(false)
        navigation.enableItemTextHiding(true)

        val interactor = ViewModelProviders.of(this).get(MainInteractor::class.java)
        MainPresenter(this, interactor)

        currentFragment.observeOn(Schedulers.io())
                .delay(this::getDelayForFragment)
                .subscribe(this::showFragment)
    }

    private fun getDelayForFragment(fragment: Fragment): Observable<Long>? {
        val time = when (fragment) {
            is MapFragment -> 100L
            else -> 0L
        }
        return Observable.timer(time, TimeUnit.MILLISECONDS)
    }

    private fun showFragment(fragment: Fragment) {
        fragment.retainInstance = true
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commitAllowingStateLoss()
    }

    override fun showView(state: MainViewState) {
        when (state) {
            is MainViewState.Map -> currentFragment.onNext(mapFragment)
            is MainViewState.EventSchedule -> currentFragment.onNext(eventScheduleFragment)
            is MainViewState.BusSchedule -> currentFragment.onNext(busScheduleFragment)
            is MainViewState.Information -> currentFragment.onNext(informationFragment)
        }
    }

    override fun selectNavigation(selectedItemId: Int) {
        navigation.selectedItemId = selectedItemId
    }

    override fun getNavigationEvents(): Observable<MenuItem> {
        return navigation.itemSelections()
    }


}
