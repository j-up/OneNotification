package com.jup.oneNotification.core.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.model.WeatherResponse
import com.jup.oneNotification.utils.JLog
import kotlinx.coroutines.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.*

@RunWith(AndroidJUnit4::class)
class OpenWeatherTest {

    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val testDispatcher = TestCoroutineDispatcher()
    private val retrofit: OpenWeather by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeather::class.java)
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun getWeatherTest() {
        CoroutineScope(testDispatcher).launch {
            val weatherResponse = getWeather(retrofit,testDispatcher)
            JLog.d(this::class.java,weatherResponse.body().toString())
        }
    }

    @Test
    fun getUnixTimeToDateTest() {
        val unixTimeArray = arrayListOf(1597633200,1597806000,1597892400,1597978800,1598065200,1598151600,1598238000)
        unixTimeArray.map {
            JLog.d(this::class.java,getUnixTimeToDate(it))
        }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private suspend fun getWeather(retrofit: OpenWeather, defaultDispatcher: CoroutineDispatcher):Response<WeatherResponse> = withContext(defaultDispatcher) {
        val weatherResponse = retrofit
            .getCurrentWeatherData("65","135","minutely",BuildConfig.OpenWeatherKey)
            .execute()

        Assert.assertEquals("code:${weatherResponse.code()} message:${weatherResponse.message()}"
            ,weatherResponse.isSuccessful,true)

        weatherResponse
        }

    private fun getUnixTimeToDate(unixTime:Int):String {
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm a")
        val date = Date(unixTime * 1000L)

        return sdf.format(date)
    }
}