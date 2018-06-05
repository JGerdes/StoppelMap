package com.jonasgerdes.stoppelmap.domain

import android.arch.lifecycle.ViewModel
import android.util.Log
import com.jonasgerdes.mvi.Flow
import com.jonasgerdes.mvi.interpret
import com.jonasgerdes.mvi.process
import com.jonasgerdes.stoppelmap.domain.processor.MapHighlighter
import com.jonasgerdes.stoppelmap.domain.MainEvent.*
import com.jonasgerdes.stoppelmap.domain.processor.FeedItemLoader
import com.jonasgerdes.stoppelmap.domain.processor.FeedProvider
import com.jonasgerdes.stoppelmap.domain.processor.MapSearchToggle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.05.2018
 */
class MainViewModel : ViewModel() {

    val events = PublishSubject.create<MainEvent>()
    val state: Observable<MainState> by lazy {
        Observable.just(MainEvent.InitialEvent())
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribe(events)
        flow.start(events)
    }

    private val flow = Flow<MainEvent, MainState>(interpret {
        when (it) {
            is MainEvent.InitialEvent -> FeedProvider.Action()
            is MapEvent.SearchFieldClickedEvent
            -> MapSearchToggle.Action(true)
            is MapEvent.OnBackPressEvent
            -> MapSearchToggle.Action(false)
            is MapEvent.MapItemClickedEvent -> MapHighlighter.Action.StallSelect(it.slug)
            is MainEvent.FeedEvent.ReloadTriggered -> FeedItemLoader.Action() and FeedProvider.Action()
        }
    }, process(
            MapSearchToggle(),
            MapHighlighter(),
            FeedProvider(),
            FeedItemLoader()
    ),
            MainReducer(),
            { Log.d("Flow", it) }
    )
}