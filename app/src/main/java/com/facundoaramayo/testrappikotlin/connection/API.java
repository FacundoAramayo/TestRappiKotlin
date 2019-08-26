package com.facundoaramayo.testrappikotlin.connection;

import com.facundoaramayo.testrappikotlin.connection.callbacks.CallbackListPlace;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface API {

    String CACHE = "Cache-Control: max-age=0";
    String AGENT = "User-Agent: Place";
    String USER_KEY = "user_key: 8dea8a8fcddaca8f9595c57281d60b18";

    /* Place API transaction ------------------------------- */

    @Headers({CACHE, AGENT, USER_KEY})
    @GET("api/v2.1/search")
    Call<CallbackListPlace> getPlacesByPage(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("radius") int radius,
            @Query("count") int count,
            @Query("sort") String sort,
            @Query("order") String order
    );

    @Headers({CACHE, AGENT, USER_KEY})
    @GET("api/v2.1/search")
    Call<CallbackListPlace> getPlacesByCategory(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("radius") int radius,
            @Query("category") int category,
            @Query("count") int count,
            @Query("sort") String sort,
            @Query("order") String order
    );




}
