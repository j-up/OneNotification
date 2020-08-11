package com.jup.oneNotification.core.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
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
    fun weatherTest() {
        getCurrentWeather(retrofit,testDispatcher)
    }

    @Test
    fun unixTimeTest() {
        val sdf = SimpleDateFormat("yyyy/MM/dd hh:mm a")
        val date = Date(1597133888 * 1000L)
        sdf.format(date)

        JLog.d(this::class.java,sdf.format(date))
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun getCurrentWeather(retrofit: OpenWeather, defaultDispatcher: CoroutineDispatcher) {
        CoroutineScope(defaultDispatcher).launch {
            val weatherResponse = retrofit.getCurrentWeatherData("65","135","hourly","ca12ccd36cad2464491aa5d2d13a53d6").execute()
            //val weatherResponse = retrofit.getCurrentWeatherData("35","139","055feb39041f68fd1ef3ed7147be39ea").execute()

            //https://api.openweathermap.org/data/2.5/onecall?lat=65&lon=135&exclude=hourly&appid=ca12ccd36cad2464491aa5d2d13a53d6

            // https://api.openweathermap.org/data/2.5/weather?lat=65&lon=135&appid=ca12ccd36cad2464491aa5d2d13a53d6
            Assert.assertEquals("code:${weatherResponse.code()} message:${weatherResponse.message()}",weatherResponse.isSuccessful,true)

            if(weatherResponse.isSuccessful) {
                JLog.d(this::class.java,"is success")
                JLog.d(this::class.java,weatherResponse.body().toString())
            }
        }
    }

}