package com.jonasgerdes.stoppelmap.about

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.res.ResourcesCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxrelay2.BehaviorRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.bus.list.RouteAdapter
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.domain.MainViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.about_fragment.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class AboutFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
    private lateinit var flowDisposable: Disposable
    private val state = BehaviorRelay.create<MainState>()

    private val cardAdapter = GroupAdapter<ViewHolder>().apply {
        add(Section().apply {
            add(AuthorCard(
                    name = "Jonas Gerdes",
                    work = "Idee, Programmierung",
                    mail = "moin@jonasgerdes.com",
                    website = "https://jonasgerdes.com",
                    githubUrl = "https://github.com/JGerdes"
            ))
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.about_fragment, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cards.adapter = cardAdapter
        bindEvents()
        toolbar.title = getString(R.string.about_toolbar_title)
        val font = ResourcesCompat.getFont(context!!, R.font.roboto_slab_light)
        toolbarLayout.setExpandedTitleTypeface(font)
        toolbarLayout.setCollapsedTitleTypeface(font)

        render(state.map { it.transportation }
                .observeOn(AndroidSchedulers.mainThread()))
    }

    @SuppressLint("CheckResult")
    private fun bindEvents() {

    }

    @SuppressLint("CheckResult")
    private fun render(state: Observable<MainState.TransportationState>) {

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