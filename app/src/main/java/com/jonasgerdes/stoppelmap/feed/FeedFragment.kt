package com.jonasgerdes.stoppelmap.event.list

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.MainEvent
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import com.jonasgerdes.stoppelmap.feed.FeedItemAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.feed_fragment.*
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class FeedFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val adapter = FeedItemAdapter()

    private lateinit var flowDisposable: Disposable
    private val state = BehaviorRelay.create<MainState>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.feed_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feed.adapter = adapter
        bindEvents()
        toolbar.title = "Neuigkeiten"
        val font = ResourcesCompat.getFont(context!!, R.font.roboto_slab_light)
        toolbarLayout.setExpandedTitleTypeface(font)
        toolbarLayout.setCollapsedTitleTypeface(font)
        render(state.map { it.feed }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    @SuppressLint("CheckResult")
    private fun bindEvents() {
        swipeRefresh.refreshes()
                .map { MainEvent.FeedEvent.ReloadTriggered() }
                .subscribe(viewModel.events)
    }

    @SuppressLint("CheckResult")
    private fun render(feed: Observable<MainState.FeedState>) {
        feed.map { it.newsItems }
                .distinctUntilChanged().subscribe {
                    adapter.submitList(it)
                }
        feed.map { it.isLoading }
                .distinctUntilChanged()
                .subscribe {
                    swipeRefresh.isRefreshing = it
                }
    }

    override fun onStart() {
        super.onStart()
        flowDisposable = viewModel.state.subscribe(state)
    }

    override fun onStop() {
        flowDisposable.dispose()
        super.onStop()
    }
}