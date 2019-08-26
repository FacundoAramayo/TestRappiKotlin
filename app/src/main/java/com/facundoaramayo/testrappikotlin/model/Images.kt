package com.facundoaramayo.testrappikotlin.model

import java.io.Serializable

class Images : Serializable {
    var place_id: Int = 0
    lateinit var imageUrl: String

    constructor() {}

    constructor(place_id: Int, name: String) {
        this.place_id = place_id
        this.imageUrl = name
    }
}
