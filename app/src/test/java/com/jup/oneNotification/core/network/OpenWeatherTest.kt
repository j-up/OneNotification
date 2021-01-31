package com.jup.oneNotification.core.network

import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.core.di.module.appModule
import com.jup.oneNotification.model.WeatherResponse
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
import java.text.SimpleDateFormat
import java.util.*

@RunWith(RobolectricTestRunner::class)
class OpenWeatherTest: KoinTest {

    private val testDispatcher = TestCoroutineDispatcher()

    private val openWeatherApi: OpenWeatherApi by inject()

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
    fun getWeatherTest() {
        CoroutineScope(testDispatcher).launch {
            val weatherResponse = getWeather(openWeatherApi,testDispatcher)

            Assert.assertEquals("code:${weatherResponse.code()} message:${weatherResponse.message()}"
                ,weatherResponse.isSuccessful,true)

            println(weatherResponse.body().toString())
        }
    }

    @Test
    fun getUnixTimeToDateTest() {
        val unixTimeArray = arrayListOf(1597633200,1597806000,1597892400,1597978800,1598065200,1598151600,1598238000)
        unixTimeArray.map {
            println(getUnixTimeToDate(it))
        }
    }



    private suspend fun getWeather(retrofit: OpenWeatherApi, defaultDispatcher: CoroutineDispatcher):Response<WeatherResponse> = withContext(defaultDispatcher) {
        val weatherResponse = retrofit
            .getCurrentWeatherData("65","135","minutely",BuildConfig.OpenWeatherKey)
            .execute()

        weatherResponse
        }

    private fun getUnixTimeToDate(unixTime:Int):String {
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm a")
        val date = Date(unixTime * 1000L)

        return sdf.format(date)
    }
}