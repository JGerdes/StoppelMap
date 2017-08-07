package com.jonasgerdes.stoppelmap.usecase.main.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.WindowManager
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
import com.jonasgerdes.stoppelmap.util.view.KeyboardUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 11.06.2017
 */
class MainActivity : AppCompatActivity(), MainView {

    private val mapFragment = MapFragment()
    private val eventScheduleFragment = EventOverviewFragment()
    private val busScheduleFragment = TransportOverviewFragment()
    private val informationFragment = InformationFragment()
    private var currentFragment = BehaviorSubject.create<Fragment>()

    private lateinit var presenter: MainPresenter
    lateinit var permissions: RxPermissions

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.enableItemShifting(false)
        navigation.enableItemTextHiding(true)

        val extraMarginBottom
                = resources.getDimensionPixelSize(R.dimen.keyboard_extra_margin_bottom)
        KeyboardUtil(this, fragmentContainer, extraMarginBottom).enable()
        permissions = RxPermissions(this)

        val interactor = ViewModelProviders.of(this).get(MainInteractor::class.java)
        presenter = MainPresenter(this, interactor)

        currentFragment
                .delay(100L, TimeUnit.MILLISECONDS)
                .doOnNext(this::showFragment)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::updateStatusBar)

    }

    private fun showFragment(fragment: Fragment) {
        fragment.retainInstance = true
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun updateStatusBar(fragment: Fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (!(fragment is MapFragment)) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.statusBarColor = ContextCompat.getColor(this, R.color.primaryDark)
            } else {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            }
        }
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

    override fun onDestroy() {
        presenter.dispose()
        super.onDestroy()
    }


}
