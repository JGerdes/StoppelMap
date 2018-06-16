package com.jonasgerdes.stoppelmap.domain

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jakewharton.rxrelay2.PublishRelay
import com.jonasgerdes.mvi.Flow
import com.jonasgerdes.mvi.interpret
import com.jonasgerdes.mvi.process
import com.jonasgerdes.stoppelmap.domain.MainEvent.*
import com.jonasgerdes.stoppelmap.domain.processor.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainViewModel : ViewModel() {

    val events = PublishRelay.create<MainEvent>()
    val state: Observable<MainState> by lazy {
        flow.start(events)
    }.apply {
        Observable.just(MainEvent.InitialEvent())
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribe(events)
    }

    private val flow = Flow<MainEvent, MainState>(interpret {
        when (it) {
            is MainEvent.InitialEvent -> FeedProvider.Action()
            is MapEvent.MapMoved -> MapHighlighter.Action.SelectNothing
            is MapEvent.SearchFieldClickedEvent
            -> MapSearchToggle.Action(true) and MapSearch.Action.Refresh()
            is MapEvent.OnBackPressEvent
            -> MapSearchToggle.Action(false)
            is MapEvent.MapItemClickedEvent -> MapHighlighter.Action.SelectNothing and
                    MapHighlighter.Action.StallSelect(it.slug)
            is MainEvent.MapEvent.QueryEntered -> MapSearch.Action.Search(it.query)
            is MainEvent.FeedEvent.ReloadTriggered -> FeedItemLoader.Action()
            is MainEvent.MapEvent.SearchResultClicked ->
                        MapHighlighter.Action.ResultSelect(it.searchResultId) and
                        MapSearchToggle.Action(false) and
                                MapHighlighter.Action.HighlightCard(1)
            is MainEvent.MapEvent.StallCardSelected ->
                MapHighlighter.Action.HighlightCard(it.cardIndex)
        }
    }, process(
            MapSearchToggle(),
            MapHighlighter(),
            MapSearch(),
            FeedProvider(),
            FeedItemLoader()
    ),
            MainReducer(),
            { Log.d("Flow", it) }
    )
}