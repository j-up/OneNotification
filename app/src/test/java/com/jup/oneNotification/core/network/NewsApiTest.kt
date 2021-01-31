package com.jup.oneNotification.core.network

import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.core.di.module.appModule
import com.jup.oneNotification.model.NewsResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import retrofit2.Response

@RunWith(RobolectricTestRunner::class)
class NewsApiTest : KoinTest {
    private val testDispatcher =  TestCoroutineDispatcher()

    private val newsApi: NewsApi by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(appModule)
    }

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
            val newResponse = getHeadlineNews(this@NewsApiTest.newsApi,testDispatcher)

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