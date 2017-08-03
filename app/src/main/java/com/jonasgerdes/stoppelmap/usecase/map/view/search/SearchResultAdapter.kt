package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.search.GroupSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.MapSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.ProductSearchResult
import com.jonasgerdes.stoppelmap.model.entity.map.search.SingleEntitySearchResult
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 06.07.17
 */
class SearchResultAdapter : RecyclerView.Adapter<SearchResultHolder<*>>() {

    private var resultList: List<MapSearchResult> = ArrayList()
    private val selectionSubject = PublishSubject.create<MapSearchResult>()

    var results
        get() = resultList
        set(value) {
            resultList = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (resultList[position]) {
            is SingleEntitySearchResult -> R.layout.map_search_result_single
            is ProductSearchResult -> R.layout.map_search_result_product
            is GroupSearchResult -> R.layout.map_search_result_group
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SearchResultHolder<*> {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        val holder = when (viewType) {
            R.layout.map_search_result_single -> SingleResultHolder(view)
            R.layout.map_search_result_product -> ProductResultHolder(view)
            R.layout.map_search_result_group -> GroupResultHolder(view)
            else -> SingleResultHolder(view)
        }
        holder.itemView.setOnClickListener({
            selectionSubject.onNext(resultList[holder.adapterPosition])
        })
        return holder
    }

    override fun onBindViewHolder(holder: SearchResultHolder<*>?, position: Int) {
        when (holder) {
            is SingleResultHolder -> holder.onBind(resultList[position] as SingleEntitySearchResult)
            is ProductResultHolder -> holder.onBind(resultList[position] as ProductSearchResult)
            is GroupResultHolder -> holder.onBind(resultList[position] as GroupSearchResult)
        }
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun selections(): Observable<MapSearchResult> {
        return selectionSubject.hide()
    }
}