package com.jonasgerdes.stoppelmap.usecase.main.view

import android.arch.lifecycle.ViewModelProviders
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.MenuItem
import android.view.WindowManager
import com.jakewharton.rxbinding2.support.design.widget.itemSelections
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.model.versioning.Release
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
import com.jonasgerdes.stoppelmap.util.versioning.VersionHelper
import com.jonasgerdes.stoppelmap.util.view.KeyboardUtil
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject


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

    private var isUpdatingNavigation = true //prevent nav selection events to be fired during change

    private lateinit var presenter: MainPresenter
    lateinit var permissions: RxPermissions

    @Inject
    protected lateinit var versionHelper: VersionHelper

    init {
        App.graph.inject(this)
        mapFragment.arguments = Bundle()
    }

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
                .subscribe { this::resetWindowFlags }

    }

    private fun showFragment(fragment: Fragment) {
        fragment.retainInstance = true
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commitAllowingStateLoss()
    }

    private fun resetWindowFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
        }
    }

    override fun showView(state: MainViewState) {
        when (state) {
            is MainViewState.Map -> {
                mapFragment.arguments.putParcelable(MapFragment.ARG_DETAIL_ENTITY_SLUG, state.slug)
                currentFragment.onNext(mapFragment)
            }
            is MainViewState.EventSchedule -> currentFragment.onNext(eventScheduleFragment)
            is MainViewState.BusSchedule -> currentFragment.onNext(busScheduleFragment)
            is MainViewState.Information -> currentFragment.onNext(informationFragment)
        }
    }

    override fun selectNavigation(selectedItemId: Int) {
        isUpdatingNavigation = true
        navigation.selectedItemId = selectedItemId
        isUpdatingNavigation = false
    }

    override fun showMessage(message: Message) {
        AlertDialog.Builder(this)
                .setTitle(message.title)
                .setMessage(Html.fromHtml(message.message))
                .setPositiveButton(R.string.dialog_version_message_positiv, null)
                .setOnDismissListener({
                    versionHelper.setHasMessageBeShown(message)
                })
                .show()
    }

    override fun showUpdateMessage(pendingUpdate: Release) {
        val message = getString(R.string.dialog_version_update_available_message,
                pendingUpdate.name

        )
        AlertDialog.Builder(this)
                .setTitle(R.string.dialog_version_update_available_title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_version_update_available_button_positive, {
                    _: DialogInterface, _: Int ->
                    try {
                        startActivity(Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse("market://details?id=$packageName")
                        ))
                    } catch (notFound: ActivityNotFoundException) {
                        startActivity(Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(
                                        "http://play.google.com/store/apps/details?id=$packageName"
                                )
                        ))
                    }
                })
                .setNegativeButton(R.string.dialog_version_update_available_button_negative, null)
                .show()
    }

    override fun getNavigationEvents(): Observable<MenuItem> {
        return navigation.itemSelections()
                .filter { !isUpdatingNavigation }
    }

    override fun getIntents(): Observable<Uri> {
        if (intent.data != null) {
            return Observable.just(intent.data)
        }
        return Observable.empty<Uri>()
    }

    override fun onDestroy() {
        presenter.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        //todo: do this via presenter/interactor
        if (currentFragment.value is MapFragment) {
            if ((currentFragment.value as MapFragment).onBackPress()) {
                return
            }
        }
        super.onBackPressed()
    }


}
