package com.facundoaramayo.testrappikotlin.model

import java.io.Serializable

class ResLocation : Serializable {

    var address: String? = null
    var locality: String? = null
    var city: String? = null
    var city_id: Int = 0
    var latitude: String? = null
    var longitude: String? = null
    var zipcode: String? = null
    var country_id: Int = 0
    var locality_verbose: String? = null
}
