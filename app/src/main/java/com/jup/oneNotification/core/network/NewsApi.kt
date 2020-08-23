package com.jup.oneNotification.core.network

import com.jup.oneNotification.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("v2/top-headlines")
    fun getHeadlineNews(@Query("country") country: String
                        , @Query("apiKey") apiKey: String): Call<NewsResponse>
}