package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.PhoneNumber
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import org.jetbrains.anko.sdk25.coroutines.onClick
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class PhoneNumberAdapter : RecyclerView.Adapter<PhoneNumberHolder>() {

    private var numberList: List<PhoneNumber> = ArrayList()
    @Inject lateinit var stringHelper: StringResourceHelper

    init {
        App.graph.inject(this)
    }

    var phoneNumbers
        get() = numberList
        set(value) {
            numberList = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PhoneNumberHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.map_entity_card_phone_numbers_number, parent, false)
        val holder = PhoneNumberHolder(view)
        //todo: move somewhere else
        view.onClick {
            val number = phoneNumbers[holder.adapterPosition]
            val callIntent = Intent(Intent.ACTION_DIAL)
            callIntent.data = Uri.parse("tel:${number.callableNumber}")
            view.context.startActivity(callIntent)

        }
        return holder
    }

    override fun onBindViewHolder(holder: PhoneNumberHolder, position: Int) {
        with(numberList[position]) {
            holder.onBind(stringHelper.getNameFor(name), prettifiedNumber, description)
        }
    }

    override fun getItemCount(): Int {
        return numberList.size
    }
}