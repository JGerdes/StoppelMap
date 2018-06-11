package com.jonasgerdes.stoppelmap.map

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.map.search.HighlightedText
import com.jonasgerdes.stoppelmap.model.map.search.SearchResult
import io.reactivex.subjects.PublishSubject
import com.jonasgerdes.stoppelmap.util.NoContentItem
import com.jonasgerdes.stoppelmap.util.setStallTypeDrawable
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.map_search_result_item_single_stall.*

class SearchResultAdapter : GroupAdapter<ViewHolder>() {

    val searchResultSection = Section().apply {
        setHeader(NoContentItem(R.layout.map_search_result_item_header))
    }

    init {
        add(searchResultSection)
        setOnItemClickListener { item, _ ->
            (item as? ResultItem<*>)?.let { selectionSubject.onNext(it.result) }
        }
    }

    private val selectionSubject = PublishSubject.create<SearchResult>()

    fun selections() = selectionSubject.hide()


    fun submitList(results: List<SearchResult>) {
        searchResultSection.update(results.mapIndexed { index, result ->
            val background = when (true) {
                results.size == 1 -> R.drawable.bg_rounded
                index == 0 -> R.drawable.bg_rounded_top
                index == results.lastIndex -> R.drawable.bg_rounded_bottom
                else -> R.drawable.bg_card_no_rounds
            }
            when (result) {
                is SearchResult.SingleStallResult -> SingleStallItem(result, background)
                is SearchResult.ItemResult -> ItemStallsItem(result, background)
            }
        })
    }

}

sealed class ResultItem<T : SearchResult> : Item() {
    abstract val result: T
}

class ItemStallsItem(override val result: SearchResult.ItemResult, val background: Int)
    : ResultItem<SearchResult.ItemResult>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.setText(result.title)
        viewHolder.icon.visibility = View.INVISIBLE
        viewHolder.subtitle.visibility = View.VISIBLE
        val withName = result.stalls.filter { it.name != null }.take(2)
        val moreCount = result.stalls.size - withName.size
        val names = withName.map { it.name }.joinToString(", ")
        if(withName.isEmpty()) {
            viewHolder.subtitle.text = "$moreCount bieten das an"
        } else {
            viewHolder.subtitle.text = "$names und $moreCount weitere"
        }
    }

    override fun getLayout() = R.layout.map_search_result_item_single_stall

}

class SingleStallItem(override val result: SearchResult.SingleStallResult, val background: Int)
    : ResultItem<SearchResult.SingleStallResult>() {
    override fun getLayout() = R.layout.map_search_result_item_single_stall

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.setText(result.title)
        viewHolder.itemView.setBackgroundResource(background)
        if (result.subtitle != null) {
            viewHolder.subtitle.apply {
                visibility = View.VISIBLE
                setText(result.subtitle)
            }
        } else {
            viewHolder.subtitle.visibility = View.GONE
        }
        viewHolder.icon.visibility = View.VISIBLE
        viewHolder.icon.setStallTypeDrawable(result.stall.type)
    }
}


fun TextView.setText(text: HighlightedText) {
    val spannable = SpannableStringBuilder(text.text)
    text.highlights.forEach {
        spannable.setSpan(StyleSpan(Typeface.BOLD), it.start, it.end,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
    setText(spannable)
}