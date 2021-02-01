package com.jup.oneNotification.core.api

import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.model.NaverNewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NaverNewsApi {
    @Headers(
        "X-Naver-Client-Id: ${BuildConfig.NaverClientId}",
        "X-Naver-Client-Secret: ${BuildConfig.NaverClientSecret}"
    )
    @GET("v1/search/news.json")
    fun getNews(@Query("query") query: String
                ,@Query("display") display: Int): Call<NaverNewsResponse>
}
