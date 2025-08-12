package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.model.MapIcon

val MapIcon.iconRes
    get() = when (this) {
        MapIcon.Bar -> R.drawable.ic_stall_type_bar
        MapIcon.CandyStall -> R.drawable.ic_stall_type_candy_stall
        MapIcon.Expo -> R.drawable.ic_stall_type_expo
        MapIcon.FoodStall -> R.drawable.ic_stall_type_food_stall
        MapIcon.GameStall -> R.drawable.ic_stall_type_game_stall
        MapIcon.Misc -> R.drawable.ic_stall_type_misc
        MapIcon.Parking -> R.drawable.ic_stall_type_parking
        MapIcon.Restaurant -> R.drawable.ic_stall_type_restaurant
        MapIcon.Restroom -> R.drawable.ic_stall_type_restroom
        MapIcon.Ride -> R.drawable.ic_stall_type_ride
        MapIcon.SellerStall -> R.drawable.ic_stall_type_seller_stall
        MapIcon.Entrance -> R.drawable.ic_entrance
        MapIcon.Station -> R.drawable.ic_bus
        MapIcon.Platform -> R.drawable.ic_train
        MapIcon.Taxi -> R.drawable.ic_taxi
        MapIcon.Kids -> R.drawable.ic_kids
        MapIcon.Wheelchair -> R.drawable.ic_wheelchair
        MapIcon.Plant -> R.drawable.ic_plant
        MapIcon.RedCross -> R.drawable.ic_red_cross
        MapIcon.Police -> R.drawable.ic_police
        MapIcon.Atm -> R.drawable.ic_atm
        MapIcon.WaterFountain -> R.drawable.ic_water_fountain
        MapIcon.TransitTickets -> R.drawable.ic_transit_ticket
        MapIcon.Bike -> R.drawable.ic_bike
    }