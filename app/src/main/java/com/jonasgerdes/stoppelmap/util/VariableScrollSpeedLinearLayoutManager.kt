package com.jonasgerdes.stoppelmap.util

import android.content.Context
import android.graphics.PointF
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics

class VariableScrollSpeedLinearLayoutManager(context: Context, orientation: Int, val factor: Float) :
        LinearLayoutManager(context, orientation, false) {
    private val scroller = object : LinearSmoothScroller(context) {

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@VariableScrollSpeedLinearLayoutManager
                    .computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
            return super.calculateSpeedPerPixel(displayMetrics) * factor
        }
    }

    override fun smoothScrollToPosition(recyclerView: RecyclerView,
                                        state: RecyclerView.State, position: Int) {

        scroller.targetPosition = position
        startSmoothScroll(scroller)
    }
}
