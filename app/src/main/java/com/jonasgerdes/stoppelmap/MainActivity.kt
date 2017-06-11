package com.jonasgerdes.stoppelmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jonasgerdes.stoppelmap.util.enableItemShifting
import com.jonasgerdes.stoppelmap.util.enableItemTextHiding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.enableItemShifting(false)
        navigation.enableItemTextHiding(true)
    }

}
