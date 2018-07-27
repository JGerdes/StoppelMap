package com.jonasgerdes.stoppelmap

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.about.AboutFragment
import com.jonasgerdes.stoppelmap.domain.MainEvent
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import com.jonasgerdes.stoppelmap.event.list.BusListFragment
import com.jonasgerdes.stoppelmap.event.list.EventListFragment
import com.jonasgerdes.stoppelmap.event.list.FeedFragment
import com.jonasgerdes.stoppelmap.map.MapFragment
import com.jonasgerdes.stoppelmap.model.news.VersionMessage
import com.jonasgerdes.stoppelmap.util.*
import com.jonasgerdes.stoppelmap.versioning.MessageItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private lateinit var flowDisposable: Disposable
    private val state = BehaviorRelay.create<MainState>()

    private val messageAdapter = GroupAdapter<ViewHolder>()

    private val fragments = mapOf(
            R.id.navigation_map to MapFragment(),
            R.id.navigation_event_schedule to EventListFragment(),
            R.id.navigation_bus_schedule to BusListFragment(),
            R.id.main_navigation_social_feed to FeedFragment(),
            R.id.main_navigation_about to AboutFragment()
    )

    private val keyboardDetector by lazy { KeyboardDetector(this) }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        bindEvents()
        initNavigation()
        toggleLayoutFullscreen(true)
        showFragment(R.id.navigation_map)

        keyboardDetector.keyboardChanges.subscribe {
            navigation.visibility = if (it) View.GONE else View.VISIBLE
        }

        messages.adapter = messageAdapter
        messages.layoutManager = VariableScrollSpeedLinearLayoutManager(this, RecyclerView.HORIZONTAL, 4f)
        LinearSnapHelper().attachToRecyclerView(messages)
        render(state.observeOn(AndroidSchedulers.mainThread()))

    }


    override fun onStart() {
        super.onStart()
        flowDisposable = viewModel.state.subscribe(state)
    }


    override fun onStop() {
        flowDisposable.dispose()
        super.onStop()
    }


    @SuppressLint("CheckResult")
    private fun render(state: Observable<MainState>) {
        state.subscribe {
            Log.d("MainActivity", "new state: $it")
        }

        state.map { it.versionInfo }
                .distinctUntilChanged()
                .map {
                    it.messages.map {
                        when (it.type) {
                            VersionMessage.TYPE_UPDATE -> MessageItem.Update(it)
                            else -> MessageItem.Styled(it)
                        }
                    }
                }
                .subscribe {
                    windowDim.visibility = if (it.isNotEmpty()) View.VISIBLE else View.GONE
                    messageAdapter.update(it)
                }
    }

    @SuppressLint("CheckResult")
    private fun bindEvents() {

        actionNext.clicks()
                .map { messages.findFirstCompletelyVisibleItemPosition() }
                .subscribe {
                    if (it < messageAdapter.itemCount - 1) {
                        messages.smoothScrollToPosition(it + 1)
                    } else {
                        windowDim.visibility = View.GONE
                    }
                }
        messages.itemScrolls()
                .log("Main") { it.toString() }
                .map { messageAdapter.getItem(it) }
                .filter { it is MessageItem }
                .map { (it as MessageItem).message.slug }
                .map { MainEvent.MessageRead(it) }
                .subscribe(viewModel.events)
    }


    private fun initNavigation() {
        navigation.enableItemShifting(false)
        navigation.enableItemTextHiding(true)
        navigation.setOnNavigationItemSelectedListener {
            showFragment(it.itemId)
            true
        }

    }

    private fun showFragment(to: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content, fragments[to])
        transaction.commit()
        ViewCompat.requestApplyInsets(content)
    }

}
