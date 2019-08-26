package com.facundoaramayo.testrappikotlin.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

import java.io.Serializable
import java.util.ArrayList

class Place : Serializable, ClusterItem {
    var place_id: Int = 0
    lateinit var name: String
    lateinit var image: String
    var address: String? = null
    var phone: String? = null
    var website: String? = null
    var description: String? = null
    var lng: Double = 0.toDouble()
    var lat: Double = 0.toDouble()
    var last_update: Long = 0
    var distance = -1f

    var categories: List<Category> = ArrayList()
    var images: List<Images> = ArrayList()

    val isDraft: Boolean
        get() = address == null && phone == null && website == null && description == null

    override fun getPosition(): LatLng {
        return LatLng(lat, lng)
    }
}
