package com.jup.oneNotification.viewModel

import android.Manifest
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.SystemClock
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jup.oneNotification.BuildConfig
import com.jup.oneNotification.R
import com.jup.oneNotification.core.common.KeyData
import com.jup.oneNotification.core.network.NewsApi
import com.jup.oneNotification.core.network.OpenWeatherApi
import com.jup.oneNotification.core.provider.LocationProvider
import com.jup.oneNotification.core.service.LocationWorker
import com.jup.oneNotification.model.AlarmDate
import com.jup.oneNotification.model.LocationModel
import com.jup.oneNotification.model.NewsResponse
import com.jup.oneNotification.model.WeatherResponse
import com.jup.oneNotification.utils.JLog
import com.jup.oneNotification.utils.PermissionUtil
import com.jup.oneNotification.view.dialog.TimePickerFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response
import java.util.*

class MainViewModel(private val timePickerDialog: TimePickerFragment
                    ,private val sharedPreferences: SharedPreferences
                    ,private val locationProvider: LocationProvider
                    ,private val permissionUtil: PermissionUtil
                    ,private val openWeatherApi: OpenWeatherApi
                    ,private val newsApi: NewsApi): ViewModel() {
    private val cal = Calendar.getInstance()
    private val _timeSetComplete = MutableLiveData<AlarmDate>()
    private val _onTimeClickListener = MutableLiveData<TimePickerFragment>()
    private val _weatherSetComplete = MutableLiveData<Int>()
    private val _locationSetComplete = MutableLiveData<String>()
    private val _fashionSetComplete = MutableLiveData<Boolean>()
    private val _permissionCheck = MutableLiveData<ArrayList<String>>()
    private val _onError = MutableLiveData<String>()

    val timeSetComplete: LiveData<AlarmDate> = _timeSetComplete
    val onTimeClickListener: LiveData<TimePickerFragment> = _onTimeClickListener
    val weatherSetComplete: LiveData<Int> = _weatherSetComplete
    val locationSetComplete: LiveData<String>  = _locationSetComplete
    val fashionSetComplete: LiveData<Boolean> = _fashionSetComplete
    val permissionCheck: LiveData<ArrayList<String>> = _permissionCheck
    val onError: MutableLiveData<String> = _onError

    private val MIN_CLICK_INTERVAL: Long = 600
    private var mLastClickTime: Long = 0


    private val permissions = arrayListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val timeSetListener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
        _timeSetComplete.value = AlarmDate(hour,minute)
        JLog.d(this::class.java,"Set Time - ${_timeSetComplete.value.toString()}")

        with(sharedPreferences.edit()) {
            putInt(KeyData.KEY_TIME_HOUR,hour).commit()
            putInt(KeyData.KEY_TIME_MINUTE,minute).commit()
        }
    }


    init{
        initTime()
        initWeather()
        initFashion()
        initLocation()
    }

    fun onTimeClick() {
        val currentClickTime = SystemClock.uptimeMillis()
        val elapsedTime = currentClickTime - mLastClickTime
        mLastClickTime = currentClickTime

        if (elapsedTime < MIN_CLICK_INTERVAL) {
            return
        }

        JLog.d(this::class.java,"onTileClick")
        timePickerDialog.listener = timeSetListener

        _onTimeClickListener.value = timePickerDialog
        _onTimeClickListener.value = null
    }

    fun onWeatherClick(radioGroup: RadioGroup, id:Int) {
        var checkValue = KeyData.VALUE_WEATHER_OPEN_WEATHER
        when(id) {
            R.id.open_weather_radio_button -> checkValue = KeyData.VALUE_WEATHER_OPEN_WEATHER
            R.id.korea_weather_radio_button -> checkValue = KeyData.VALUE_WEATHER_KOREA_WEATHER
        }

        with(sharedPreferences.edit()) {
            putInt(KeyData.KEY_WEATHER, checkValue).commit()
        }
    }

    fun onLocationClick() {
        val notPermissionList = permissionUtil.checkPermissions(permissions)
        JLog.d(this::class.java, notPermissionList.toString())
        var locationModel:LocationModel? = null

        when(notPermissionList.size) {
            0 -> locationModel = locationProvider.onLocation()
            else -> {
                _permissionCheck.value = notPermissionList
                return
            }
        }

        when(locationModel?.locationConst) {
            LocationWorker.LocationConst.SUCCESS_GET_LOCATION -> {
                var addressList = listOf(
                    locationModel.address?.adminArea
                    ,locationModel.address?.locality
                    ,locationModel.address?.thoroughfare)

                var address = addressList.filterNotNull()
                    .joinToString(" ")

                with(sharedPreferences.edit()) {
                    putString(KeyData.KEY_LOCATION,address).commit()
                    putString(KeyData.KEY_LOCATION_LAT,locationModel.address?.latitude.toString())
                    putString(KeyData.KEY_LOCATION_LON,locationModel.address?.longitude.toString())
                }
                _locationSetComplete.value = address
            }

            LocationWorker.LocationConst.DISABLE_NETWORK_PROVIDER -> {
                JLog.e(this::class.java, "Network is disable")
                _onError.value = "네트워크가 정상적으로 연결되어있는지 확인해주세요."
            }

            LocationWorker.LocationConst.DISABLE_GPS_PROVIDER -> {
                JLog.e(this::class.java, "GPS is disable")
                _onError.value = "GPS가 정상적으로 켜져있는지 확인해주세요."
            }
            else -> {
                JLog.e(this::class.java, "getLocation is error")
                _onError.value = "위치 획득에 실패하였습니다."
            }
        }
    }

    fun onFashionClick(view: CompoundButton, isChecked:Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(KeyData.KEY_FASHION, isChecked).commit()
        }
    }

    fun onTestNotiClick() {
        CoroutineScope(Dispatchers.Main).launch {
            var weatherResponse:Response<WeatherResponse>? = null
            var newsResponse:Response<NewsResponse>? = null

            val weatherJob = async(Dispatchers.IO) {
                weatherResponse = getWeather()
            }

            val newsJob = async(Dispatchers.IO) {
                newsResponse = getNews()
            }
            weatherJob.await()
            newsJob.await()

            JLog.d(this::class.java,"weather isSuccess: ${weatherResponse?.isSuccessful}")
            JLog.d(this::class.java,"news isSuccess: ${newsResponse?.isSuccessful}")

        }
    }

    private fun getWeather(): Response<WeatherResponse> {
        val lat = sharedPreferences.getString(KeyData.KEY_LOCATION_LAT,"0")?: "0"
        val lon = sharedPreferences.getString(KeyData.KEY_LOCATION_LON,"0")?: "0"

        return openWeatherApi
            .getCurrentWeatherData(lat, lon, "minutely", BuildConfig.OpenWeatherKey)
            .execute()
    }

    private fun getNews(): Response<NewsResponse> {
        return newsApi
            .getHeadlineNews("kr",BuildConfig.NewsKey)
            .execute()
    }

    private fun initTime() {
        val hour = sharedPreferences.getInt(KeyData.KEY_TIME_HOUR, 404)
        val minute = sharedPreferences.getInt(KeyData.KEY_TIME_HOUR, 404)

        // 값이 없을때 현재 시간으로 초기화
        if (hour == 404 || minute == 404) {
            with(sharedPreferences.edit()) {
                putInt(KeyData.KEY_TIME_HOUR, cal.get(Calendar.HOUR)).commit()
                putInt(KeyData.KEY_TIME_MINUTE, cal.get(Calendar.MINUTE)).commit()
            }
        }

        AlarmDate(
            sharedPreferences.getInt(KeyData.KEY_TIME_HOUR, 0),
            sharedPreferences.getInt(KeyData.KEY_TIME_MINUTE, 0)
        ).let {
            _timeSetComplete.value = it
        }
    }


    private fun initWeather() {
        var weatherValue = sharedPreferences.getInt(KeyData.KEY_WEATHER,404)

        // 값이 없을때 openWeather 초기화
        if (weatherValue == 404) {
            with(sharedPreferences.edit()) {
                putInt(KeyData.KEY_WEATHER, KeyData.VALUE_WEATHER_OPEN_WEATHER).commit()
            }
            weatherValue = KeyData.VALUE_WEATHER_OPEN_WEATHER
        }
            _weatherSetComplete.value = weatherValue
    }


    private fun initFashion() {
        val value = sharedPreferences.getBoolean(KeyData.KEY_FASHION,false)
        if(value) _fashionSetComplete.value = value
    }

    private fun initLocation() {
        val address = sharedPreferences.getString(KeyData.KEY_LOCATION,"")
        JLog.d(this::class.java,"address = $address")
        address?.let {
            if(it.isNotEmpty()) {
                _locationSetComplete.value = it
            }
        }
    }
}

