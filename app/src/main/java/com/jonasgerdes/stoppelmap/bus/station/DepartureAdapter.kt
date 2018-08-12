package com.jonasgerdes.stoppelmap.bus.station

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.inflate
import kotlinx.android.synthetic.main.bus_station_departure_item_slot_label.view.*
import kotlinx.android.synthetic.main.bus_station_departure_item_span.view.*
import kotlinx.android.synthetic.main.bus_station_departure_item_time.view.*
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
            is DepartureGridItem.TimeSlotLabel -> bind(departureGridItem)
            is DepartureGridItem.Departure -> bind(departureGridItem)
            DepartureGridItem.Empty -> bindEmpty()
        }

        fun bind(timeSpanHeader: DepartureGridItem.TimeSpanHeader) = itemView.apply {
            title.setText(timeSpanHeader.type.stringRes)
        }

        fun bind(timeSlotLabel: DepartureGridItem.TimeSlotLabel) = itemView.apply {
            label.text = timeSlotLabel.label
        }

        fun bind(departure: DepartureGridItem.Departure) = itemView.apply {
            time.text = departure.departure.format(format)
        }

        fun bindEmpty() = itemView.apply {

        }

    }

}

fun DepartureGridItem.getViewType() = when (this) {
    is DepartureGridItem.TimeSpanHeader -> R.layout.bus_station_departure_item_span
    is DepartureGridItem.TimeSlotLabel -> R.layout.bus_station_departure_item_slot_label
    is DepartureGridItem.Departure -> R.layout.bus_station_departure_item_time
    DepartureGridItem.Empty -> R.layout.bus_station_departure_item_empty
}

fun DepartureGridItem.getSpanSize() = when (this) {
    is DepartureGridItem.TimeSpanHeader -> 6
    else -> 1
}

val TimeSpan.Type.stringRes: Int
    get() = when (this) {
        TimeSpan.Type.MORNING -> R.string.transportation_station_span_morning
        TimeSpan.Type.AFTERNOON -> R.string.transportation_station_span_afternoon
        TimeSpan.Type.EVENING -> R.string.transportation_station_span_evening
        TimeSpan.Type.NIGHT -> R.string.transportation_station_span_night
    }