package com.facundoaramayo.testrappikotlin.data

object AppConfig {

    // flag for save image offline
    const val IMAGE_CACHE = true

    // if place data more than 200 items set TRUE
    const val LAZY_LOAD = false

    // when user enable gps, places will sort by distance
    const val SORT_BY_DISTANCE = true

    // distance metric, fill with KILOMETER or MILE only
    const val DISTANCE_METRIC_CODE = "KILOMETER"

    // related to UI display string
    const val DISTANCE_METRIC_STR = "Km"


}
