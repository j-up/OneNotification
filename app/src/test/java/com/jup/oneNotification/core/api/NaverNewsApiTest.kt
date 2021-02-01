package com.jup.oneNotification.core.api

import com.jup.oneNotification.core.di.module.appModule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class NaverNewsApiTest : KoinTest {

    private val naverNewsApi: NaverNewsApi by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(appModule)
    }

    @Test
    fun getNews() = runBlockingTest {
        val test = naverNewsApi
            .getNews("뉴스", 100)
            .execute()

        Assert.assertEquals(
            "code:${test?.code()} message:${test?.message()}"
            , test?.isSuccessful, true)

        println(test.body().items.map {
            it.title + "\n"
        })

    }


}