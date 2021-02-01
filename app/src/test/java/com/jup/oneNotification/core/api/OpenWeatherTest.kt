package com.jup.oneNotification.core.api

import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.core.di.module.appModule
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject
import org.robolectric.RobolectricTestRunner
import java.text.SimpleDateFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class OpenWeatherTest: KoinTest {

    private val openWeatherApi: OpenWeatherApi by inject()

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        printLogger()
        modules(appModule)
    }

    @Test
    fun getWeather() = runBlockingTest {
        val weatherResponse = openWeatherApi
            .getCurrentWeatherData("65", "135", "minutely", BuildConfig.OpenWeatherKey)
            .execute()

        Assert.assertEquals(
            "code:${weatherResponse.code()} message:${weatherResponse.message()}"
            , weatherResponse.isSuccessful, true
        )

        println(weatherResponse.body().toString())
    }

    @Test
    fun getUnixTimeToDateTest() {
        val unixTimeArray = arrayListOf(1597633200,1597806000,1597892400,1597978800,1598065200,1598151600,1598238000)
        unixTimeArray.map {
            println(getUnixTimeToDate(it))
        }
    }

    private fun getUnixTimeToDate(unixTime:Int):String {
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm a")
        val date = Date(unixTime * 1000L)

        return sdf.format(date)
    }
}