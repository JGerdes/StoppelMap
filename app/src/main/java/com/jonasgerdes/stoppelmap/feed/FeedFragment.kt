package com.jonasgerdes.stoppelmap.event.list

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.MainEvent
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.event_list_fragment.*
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

    private lateinit var flowDisposable: Disposable
    private val state = BehaviorRelay.create<MainState>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.feed_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
        render(state.map { it.feed }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    private fun bindEvents() {
        view!!.clicks()
                .map { MainEvent.FeedEvent.ReloadTriggered() }
                .subscribe(viewModel.events)
    }

    @SuppressLint("CheckResult")
    private fun render(feed: Observable<MainState.FeedState>) {
        feed.map { it.newsItems }
                .distinctUntilChanged().subscribe {
                    data.text = it.map {
                        "[${it.feedItem?.publishDate ?: "null"}]${it.feedItem?.title
                                ?: "null"} (${it.feedItem?.url ?: "null"})"
                    }.joinToString("\n")
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