package com.jonasgerdes.stoppelmap.settings.data

enum class DateOverride {
    None,
    TodayInStoMaWeek,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday,
    Monday,
    Tuesday,
    ;

    companion object {
        val default = None
    }
}
