package com.jonasgerdes.stoppelmap.map

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.annotation.AnimRes
import android.support.constraint.ConstraintLayout
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.MainState
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.map_fragment.view.*
import java.util.concurrent.TimeUnit
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import com.jonasgerdes.stoppelmap.util.*


@SuppressLint("InlinedApi", "CheckResult")
fun renderSearch(activity: Activity?, view: View?, adapter: SearchResultAdapter,
                 state: Observable<MainState.MapState>) {

    state.map { it.results }
            .subscribe { adapter.submitList(it) }


    val searchExtendedChanged = state
            .map { it.searchExtended }
            .distinctUntilChanged()

    view?.apply {
        state.map { it.showNoResultMessage && it.searchExtended }
                .distinctUntilChanged()
                .subscribe {
                    noResultMessage.visibility = if (it) View.VISIBLE else View.GONE
                }

        state.map { it.showEmptyQueryMessage && it.searchExtended }
                .distinctUntilChanged()
                .subscribe {
                    emptyQueryMessage.visibility = if (it) View.VISIBLE else View.GONE
                }

        state.map { !it.showEmptyQueryMessage && it.searchExtended }
                .distinctUntilChanged()
                .subscribe {
                    searchResults.visibility = if (it) View.VISIBLE else View.GONE
                }

        state.map { it.isPending && it.searchExtended }
                .distinctUntilChanged()
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    searchProgress.visibility = if (it) View.VISIBLE else View.GONE
                }

        searchExtendedChanged
                .filter { !it }
                .subscribe {
                    activity?.hideSoftkeyboard()
                    search.clearFocus()
                }

        searchExtendedChanged
                .map { if (it) R.anim.slide_up else R.anim.slide_down }
                .subscribe {
                    searchBackground.startAnimation(it)
                    //these seem to be buggy in current version of constraint layout:
                    /*noResultMessage.startAnimation(it)
                    emptyQueryMessage.startAnimation(it)
                    searchResults.startAnimation(it)*/
                }

        searchExtendedChanged
                .filter { Build.VERSION.SDK_INT >= Build.VERSION_CODES.M }
                .delay(100, TimeUnit.MILLISECONDS)
                .delayIf(150, TimeUnit.MILLISECONDS) { it }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    activity?.window?.apply {
                        statusBarColor = context.getColorFromTheme(if (it) {
                            R.attr.windowBackgroundColor
                        } else {
                            R.attr.translucentStatusBarColor
                        })
                    }
                    activity?.window?.decorView?.apply {
                        systemUiVisibility = if (it) {
                            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        } else {
                            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        }
                    }
                }

        searchExtendedChanged
                .map { if (it) Pair(32.dp, 0.dp) else Pair(0.dp, 32.dp) }
                .subscribe {
                    ObjectAnimator.ofFloat(search.background, "cornerRadius",
                            it.first.toFloat(), it.second.toFloat())
                            .setDuration(200)
                            .start()
                }

        searchExtendedChanged
                .map { if (it) Pair(4.dp, 2.dp) else Pair(2.dp, 4.dp) }
                .subscribe {
                    ObjectAnimator.ofFloat(search, "elevation",
                            it.first.toFloat(), it.second.toFloat())
                            .setDuration(200)
                            .start()
                }

        searchExtendedChanged
                .map { if (it) Pair(8.dp, 12.dp) else Pair(12.dp, 8.dp) }
                .subscribe {
                    ValueAnimator.ofInt(it.first, it.second)
                            .setDuration(200)
                            .apply {
                                addUpdateListener {
                                    search.setPadding(
                                            search.paddingLeft,
                                            it.animatedValue as Int,
                                            search.paddingRight,
                                            it.animatedValue as Int
                                    )
                                }
                            }.start()
                }

        searchExtendedChanged
                .map { if (it) Pair(16.dp, 4.dp) else Pair(4.dp, 16.dp) }
                .subscribe {
                    ValueAnimator.ofInt(it.first, it.second)
                            .setDuration(200)
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
}

private fun View.startAnimation(@AnimRes animation: Int) {
    startAnimation(AnimationUtils.loadAnimation(context, animation).apply {
        duration = 200
        fillAfter = true
    })
}