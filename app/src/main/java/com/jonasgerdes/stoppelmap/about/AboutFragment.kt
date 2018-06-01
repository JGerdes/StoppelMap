package com.jonasgerdes.stoppelmap.about

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.StoppelMapDatabase

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 19.04.2018
 */
class AboutFragment : Fragment() {

    private val db by lazy { StoppelMapDatabase.database }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.about_fragment, container, false)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.setOnClickListener {
            val stalls = db.stalls().getAll()
            Log.d("AboutFragment", "${stalls.size} stalls found")
            stalls.forEach {
                Log.d("AboutFragment", "${it.name}, ${it.description}")
            }
        }
    }

}