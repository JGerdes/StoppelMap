package com.jonasgerdes.stoppelmap.map.entity

sealed class Message {
    object NotInArea: Message()
}