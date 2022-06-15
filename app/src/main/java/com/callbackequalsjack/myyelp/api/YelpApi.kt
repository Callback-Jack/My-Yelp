package com.callbackequalsjack.myyelp.api

import com.callbackequalsjack.myyelp.data.Businesses
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

const val API_KEY = "Bearer 2YEj6Vcy-AcHANVK5REgDJb9vTnXyTZgtEEbBvxn5X3orHGHTFBL9P4phSj8r-y7pMWQH_e2_YuByOV6PznxIT2xd7N7poVyoNO7gQphq7VNYOhQnUnCHOIulMijYnYx"

interface YelpApi {
    @Headers ("Authorization: $API_KEY")
    @GET("/v3/businesses/search")
    suspend fun getAll(
        @Query("location") location: String,
        @Query("term") term: String
    ) : Response<Businesses>

}