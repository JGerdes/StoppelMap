package com.jonasgerdes.stoppelmap.map.entity

import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType

data class FullStall(
    val basicInfo: Stall,
    val subTypes: List<SubType>
)