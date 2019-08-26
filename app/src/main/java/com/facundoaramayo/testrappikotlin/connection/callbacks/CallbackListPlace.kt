package com.facundoaramayo.testrappikotlin.connection.callbacks

import java.io.Serializable
import java.util.ArrayList


import com.facundoaramayo.testrappikotlin.model.RestaurantContainer

class CallbackListPlace : Serializable {

    var results_found = -1
    var results_start = -1
    var results_shown = -1
    var restaurants: List<RestaurantContainer> = ArrayList()

}
