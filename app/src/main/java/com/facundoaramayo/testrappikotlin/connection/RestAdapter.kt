package com.facundoaramayo.testrappikotlin.connection

import com.facundoaramayo.testrappikotlin.BuildConfig
import com.facundoaramayo.testrappikotlin.data.Constant
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import java.util.concurrent.TimeUnit

object RestAdapter {

    fun createAPI(): API {

        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .cache(null)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constant.WEB_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(okHttpClient)
            .build()

        return retrofit.create(API::class.java)
    }

}
