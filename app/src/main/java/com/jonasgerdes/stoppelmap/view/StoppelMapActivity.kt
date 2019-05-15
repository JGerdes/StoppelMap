package com.jonasgerdes.stoppelmap.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jonasgerdes.stoppelmap.R
import kotlinx.android.synthetic.main.activity_stoppelmap.*

class StoppelMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stoppelmap)

        navigation.itemIconTintList = null
    }
}
