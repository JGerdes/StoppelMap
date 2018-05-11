package com.jonasgerdes.stoppelmap.map

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.domain.MainEvent
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import com.jonasgerdes.stoppelmap.util.dp
import com.jonasgerdes.stoppelmap.util.getColorCompat
import com.jonasgerdes.stoppelmap.util.toggleLayoutFullscreen
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.maps.MapboxMap
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.map_fragment.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class MapFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var flowDisposable: Disposable
    private val state = BehaviorRelay.create<MainState>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.map_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)
        activity?.toggleLayoutFullscreen(true)

        mapView.setStyleUrl("asset://style-light.json")
        mapView.getMapAsync {
            initMapUi(it)
            initMapCamera(it)
        }
        search.isFocusableInTouchMode = true
        mapView.isFocusable = true
        mapView.isFocusableInTouchMode = true
        search.clearFocus()

        bindEvents()

        render(state.map { it.map }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    private fun initMapCamera(it: MapboxMap) {
        it.setLatLngBoundsForCameraTarget(Settings.cameraBounds)
        it.setMinZoomPreference(Settings.minZoom)
        it.setMaxZoomPreference(Settings.maxZoom)
        it.moveCamera(CameraUpdateFactory.newLatLngZoom(
                Settings.center,
                Settings.defaultZoom
        ))
    }

    private fun initMapUi(it: MapboxMap) {
        it.uiSettings.isTiltGesturesEnabled = false
        it.uiSettings.isAttributionEnabled = false
        it.uiSettings.isLogoEnabled = false
        it.uiSettings.setCompassMargins(16.dp, (24 + 64).dp, 16.dp, 16.dp)
        it.uiSettings.compassImage = context?.getDrawable(R.drawable.ic_navigation_black_24dp)
                ?.apply {
                    context?.getColorCompat(R.color.colorPrimary)?.let { color -> setTint(color) }
                }
    }

    private fun bindEvents() {
        search.setOnClickListener {
            //todo use RxViewBindings
            viewModel.events.onNext(MainEvent.MapEvent.SearchFieldClickedEvent())
        }
        search.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                viewModel.events.onNext(MainEvent.MapEvent.SearchFieldClickedEvent())
            }
        }
        search.setOnKeyListener { _, keyCode, event ->
            Log.d("MapFragment", "onKeyEvent: ${event.action}; $keyCode")
            if(event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                viewModel.events.onNext(MainEvent.MapEvent.OnBackPressEvent())
            }
            true
        }
    }

    @SuppressLint("CheckResult")
    private fun render(state: Observable<MainState.MapState>) {
        state.map { it.searchExtended }
                .map { if (it) R.anim.slide_up else R.anim.slide_down }
                .subscribe {
                    searchBackground.startAnimation(
                            AnimationUtils.loadAnimation(context, it).apply {
                                duration = 200
                                fillAfter = true
                            }
                    )
                }
        state.map { it.searchExtended }
                .map { if (it) Pair(32.dp, 0.dp) else Pair(0.dp, 32.dp) }
                .subscribe {
                    ObjectAnimator.ofFloat(search.background, "cornerRadius",
                            it.first.toFloat(), it.second.toFloat())
                            .setDuration(300)
                            .start()
                }
        state.map { it.searchExtended }
                .map { if (it) Pair(16.dp, 4.dp) else Pair(4.dp, 16.dp) }
                .subscribe {
                    ValueAnimator.ofInt(it.first, it.second)
                            .setDuration(300)
                            .apply {
                                addUpdateListener {
                                    val margin = it.animatedValue as Int
                                    search.layoutParams = (search.layoutParams as ConstraintLayout.LayoutParams).apply {
                                        setMargins(margin, margin, margin, margin)
                                        marginStart = margin
                                        marginEnd = margin
                                    }
                                }
                                start()
                            }
                }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        flowDisposable = viewModel.state.subscribe(state)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
        search.clearFocus()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView?.onStop()
        flowDisposable.dispose()
        super.onStop()
    }

    override fun onDestroyView() {
        mapView?.onDestroy()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }


}
