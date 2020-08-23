package com.jup.oneNotification.core.network

import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.model.NewsResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
class NewsApiTest {
    private val testDispatcher =  TestCoroutineDispatcher()
    private val retrofit =
        Retrofit.Builder()
            .baseUrl("http://newsapi.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NewsApi::class.java)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getHeadlineNewsTest() {
        CoroutineScope(testDispatcher).launch {
            val newResponse = getHeadlineNews(retrofit,testDispatcher)

            Assert.assertEquals("code:${newResponse?.code()} message:${newResponse?.message()}"
                ,newResponse?.isSuccessful,true)

            newResponse?.body()?.articles?.map {
                println(it.title)
            }
        }
    }

    private suspend fun getHeadlineNews(retrofit: NewsApi, defaultDispatcher: CoroutineDispatcher): Response<NewsResponse>? = withContext(defaultDispatcher) {
        val newsResponse = retrofit
            .getHeadlineNews("kr",BuildConfig.NewsKey)
            .execute()

        newsResponse
    }

}