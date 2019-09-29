package com.facundoaramayo.testrappikotlin.connection

import com.facundoaramayo.testrappikotlin.connection.callbacks.CallbackListPlace
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface API {

    /* Place API transaction ------------------------------- */

    @Headers(CACHE, AGENT, USER_KEY)
    @GET("api/v2.1/search")
    fun getPlacesByPage(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("radius") radius: Int,
        @Query("count") count: Int,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Call<CallbackListPlace>

    @Headers(CACHE, AGENT, USER_KEY)
    @GET("api/v2.1/search")
    fun getPlacesByCategory(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("radius") radius: Int,
        @Query("category") category: Int,
        @Query("count") count: Int,
        @Query("sort") sort: String,
        @Query("order") order: String
    ): Call<CallbackListPlace>

    companion object {

        const val CACHE = "Cache-Control: max-age=0"
        const val AGENT = "User-Agent: Place"
        const val USER_KEY = "user_key: 8dea8a8fcddaca8f9595c57281d60b18"
    }


}
