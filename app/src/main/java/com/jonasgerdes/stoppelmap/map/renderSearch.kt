package com.jonasgerdes.stoppelmap.map

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.support.constraint.ConstraintLayout
import android.view.View
import android.view.animation.AnimationUtils
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.MainState
import com.jonasgerdes.stoppelmap.util.delayIf
import com.jonasgerdes.stoppelmap.util.dp
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.map_fragment.view.*
import java.util.concurrent.TimeUnit

@SuppressLint("InlinedApi")
fun renderSearch(activity: Activity?, view: View?, state: Observable<MainState.MapState>) {
    view?.apply {
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
                .delayIf(200, TimeUnit.MILLISECONDS) { it }
                .observeOn(AndroidSchedulers.mainThread())
                .filter { Build.VERSION.SDK_INT >= Build.VERSION_CODES.M }
                .subscribe {
                    activity?.window?.decorView?.apply {
                        systemUiVisibility = if (it) {
                            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                        } else {
                            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                        }
                    }
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
}