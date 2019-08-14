package com.jonasgerdes.stoppelmap.domain

import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider

class StoppelmarktInfoProvider : GlobalInfoProvider {
    override fun getAreaBounds(): GlobalInfoProvider.AreaBounds = GlobalInfoProvider.AreaBounds(
        northLatitude = 52.7494011815008,
        southLatitude = 52.7429499584193,
        westLongitude = 8.28653654801576,
        eastLongitude = 8.30059127365977
    )

}