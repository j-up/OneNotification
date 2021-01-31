package com.jup.oneNotification.core.network

import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.core.di.module.appModule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NewsApiTest : KoinTest {

    private val newsApi: NewsApi by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(appModule)
    }

    @Test
    fun getHeadlineNews() = runBlockingTest {
        val newResponse = newsApi
            .getHeadlineNews("kr", BuildConfig.NewsKey)
            .execute()

        Assert.assertEquals(
            "code:${newResponse?.code()} message:${newResponse?.message()}"
            , newResponse?.isSuccessful, true)

        newResponse?.body()?.articles?.map {
            println(it.title)
        }
    }


}