package com.jonasgerdes.stoppelmap.map.view

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import androidx.core.view.isVisible
import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.entity.HighlightedText
import com.jonasgerdes.stoppelmap.map.entity.SearchResult
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_search_result.view.*


data class StallSearchResultItem(
    val result: SearchResult.StallSearchResult
) : Item() {

    override fun getLayout() = R.layout.item_search_result

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = result.title.asSpannable()
            when (val subtitleText = result.subtitle) {
                null -> subtitle.isVisible = false
                else -> {
                    subtitle.isVisible = true
                    subtitle.text = subtitleText.asSpannable()
                }
            }
            icon.setStallTypeDrawable(result.stall.type)

        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is StallSearchResultItem) {
            return false
        }
        return other.result == result
    }

    private fun HighlightedText.asSpannable() = SpannableStringBuilder(text).apply {
        highlights.forEach {
            setSpan(
                StyleSpan(Typeface.BOLD), it.start, it.end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
}