package com.facundoaramayo.testrappikotlin.model

import java.io.Serializable

class RestaurantItem : Serializable {

    //private List<Review> all_reviews;
    var all_reviews_count: Int = 0
    var average_cost_for_two: Int = 0
    var cuisines: String? = null
    var currency: String? = null
    var deeplink: String? = null
    var events_url: String? = null
    var featured_image: String? = null
    var has_online_delivery: Int = 0
    var has_table_booking: Int = 0
    var id: Int = 0
    var is_delivering_now: Int = 0
    var location: ResLocation? = null
    var menu_url: String? = null
    var name: String? = null
    var phone_numbers: String? = null
    var photo_count: Int = 0
    //private List<Photo> photos;
    var photos_url: String? = null
    var price_range: Int = 0
    var thumb: String? = null
    var url: String? = null
}
