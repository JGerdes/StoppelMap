package com.jonasgerdes.stoppelmap.transport.view.station

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jonasgerdes.androidutil.inflate
import com.jonasgerdes.stoppelmap.transport.R
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullStationUseCase
import kotlinx.android.synthetic.main.item_departure_time.view.*
import kotlinx.android.synthetic.main.item_departure_time_of_day_title.view.*
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class DepartureAdapter : RecyclerView.Adapter<DepartureAdapter.Holder>() {

    var items = emptyList<DepartureGridItem>()
        private set

    fun submitList(list: List<DepartureGridItem>) {
        items = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(parent.inflate(viewType))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position])

    }

    inner class Holder(view: View) : RecyclerView.ViewHolder(view) {

        val format = DateTimeFormatter.ofPattern("HH:mm")

        fun bind(departureGridItem: DepartureGridItem) = when (departureGridItem) {
            is DepartureGridItem.TimeSpanHeader -> bind(departureGridItem)
            is DepartureGridItem.Departure -> bind(departureGridItem)
            DepartureGridItem.Empty -> bindEmpty()
        }

        fun bind(timeSpanHeader: DepartureGridItem.TimeSpanHeader) = itemView.apply {
            title.setText(timeSpanHeader.type.getTitle())
        }

        fun bind(departure: DepartureGridItem.Departure) = itemView.apply {
            time.text = departure.departure.format(format)
        }

        fun bindEmpty() = itemView.apply {

        }

    }

}

fun DepartureGridItem.getViewType() = when (this) {
    is DepartureGridItem.TimeSpanHeader -> R.layout.item_departure_time_of_day_title
    is DepartureGridItem.Departure -> R.layout.item_departure_time
    DepartureGridItem.Empty -> R.layout.item_departure_empty
}

fun DepartureGridItem.getSpanSize() = when (this) {
    is DepartureGridItem.TimeSpanHeader -> 6
    else -> 1
}

fun GetFullStationUseCase.DayTimeSlot.Type.getTitle() =
    when (this) {
        GetFullStationUseCase.DayTimeSlot.Type.MORNING -> R.string.transport_slot_morning
        GetFullStationUseCase.DayTimeSlot.Type.AFTERNOON -> R.string.transport_slot_afternoon
        GetFullStationUseCase.DayTimeSlot.Type.EVENING -> R.string.transport_slot_evening
        GetFullStationUseCase.DayTimeSlot.Type.NIGHT -> R.string.transport_slot_night
    }


sealed class DepartureGridItem {
    data class TimeSpanHeader(val type: GetFullStationUseCase.DayTimeSlot.Type) : DepartureGridItem()
    data class Departure(val departure: OffsetDateTime) : DepartureGridItem()
    object Empty : DepartureGridItem()
}