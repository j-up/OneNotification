package com.jup.oneNotification.core.network

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jup.oneNotification.model.WeatherResponse
import com.jup.oneNotification.utils.JLog
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    fun main() {
        getCurrentWeather(retrofit,testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    private fun getCurrentWeather(retrofit: OpenWeather, defaultDispatcher: CoroutineDispatcher) {
        CoroutineScope(defaultDispatcher).launch {
            val weatherResponse = retrofit.getCurrentWeatherData("137","65","ca12ccd36cad2464491aa5d2d13a53d6")
            weatherResponse.enqueue(object : Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>?, response: Response<WeatherResponse>?) {
                    Assert.assertEquals("fail network",response?.code(),200)
                    JLog.d(this::class.java,response?.body().toString())
                    JLog.d(this::class.java,"aaa" + response?.body().toString())

                }

                override fun onFailure(call: Call<WeatherResponse>?, t: Throwable?) {
                    JLog.d(this::class.java,t.toString())
                }
            })

        }
    }

}