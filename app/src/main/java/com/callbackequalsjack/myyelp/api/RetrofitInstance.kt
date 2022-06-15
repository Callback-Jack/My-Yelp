package com.callbackequalsjack.myyelp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: YelpApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.yelp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YelpApi::class.java)
    }
}