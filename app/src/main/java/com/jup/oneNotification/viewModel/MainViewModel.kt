package com.jup.oneNotification.viewModel

import android.Manifest
import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.SystemClock
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jup.oneNotification.R
import com.jup.oneNotification.core.common.KeyData
import com.jup.oneNotification.core.provider.LocationProvider
import com.jup.oneNotification.core.service.LocationWorker
import com.jup.oneNotification.model.AlarmDate
import com.jup.oneNotification.model.LocationModel
import com.jup.oneNotification.utils.JLog
import com.jup.oneNotification.utils.RequestPermission
import com.jup.oneNotification.view.dialog.TimePickerFragment
import java.util.*


class MainViewModel(private val timePickerDialog: TimePickerFragment
                    ,private val sharedPreferences: SharedPreferences
                    ,private val locationProvider: LocationProvider
                    ,private val requestPermission: RequestPermission): ViewModel() {
    private val cal = Calendar.getInstance()
    private val _timeSetComplete = MutableLiveData<AlarmDate>()
    private val _onTimeClickListener = MutableLiveData<TimePickerFragment>()
    private val _weatherSetComplete = MutableLiveData<Int>()
    private val _locationSetComplete = MutableLiveData<String>()
    private val _newsSetComplete = MutableLiveData<ArrayList<String>>()
    private val _fashionSetComplete = MutableLiveData<Boolean>()
    private val _permissionCheck = MutableLiveData<ArrayList<String>>()
    private val _onError = MutableLiveData<String>()

    val timeSetComplete: LiveData<AlarmDate> = _timeSetComplete
    val onTimeClickListener: LiveData<TimePickerFragment> = _onTimeClickListener
    val weatherSetComplete: LiveData<Int> = _weatherSetComplete
    val locationSetComplete: LiveData<String>  = _locationSetComplete
    val newsSetComplete: LiveData<ArrayList<String>> =_newsSetComplete
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
        initNews()
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
        val notPermissionList = requestPermission.checkPermissions(permissions)
        JLog.d(this::class.java, notPermissionList.toString())
        var locationModel:LocationModel? = null

        when(notPermissionList.size) {
            0 -> locationModel = locationProvider.onLocation()
            else -> _permissionCheck.value = notPermissionList
        }

        when(locationModel?.locationConst) {
            LocationWorker.LocationConst.SUCCESS_GET_LOCATION -> {
                val address = locationModel.address?.let {
                    "${it.adminArea} ${it.locality} ${it.thoroughfare}"
                }

                with(sharedPreferences.edit()) {
                    putString(KeyData.KEY_LOCATION,address).commit()
                }
                _locationSetComplete.value = address
            }
            else -> {
                JLog.e(this::class.java, "getLocation is error")
                _onError.value = "위치 획득에 실패하였습니다."
            }
        }
    }

    fun onNewsClick(view: View) {
        var checked: Boolean = false

        if(view is CheckBox) {
            checked = view.isChecked
        }

        when(view.id) {
            R.id.cho_check_box -> with(sharedPreferences.edit()) {
                putBoolean(KeyData.KEY_NEWS_CHO, checked).commit()
            }

            R.id.khan_check_box -> with(sharedPreferences.edit()) {
                putBoolean(KeyData.KEY_NEWS_KHAN, checked).commit()
            }
        }
    }

    fun onFashionClick(view: CompoundButton, isChecked:Boolean) {
        with(sharedPreferences.edit()) {
            putBoolean(KeyData.KEY_FASHION, isChecked).commit()
        }
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


    private fun initNews() {
        val valueMap = mutableMapOf<String,Boolean>()
        valueMap[KeyData.KEY_NEWS_CHO] = sharedPreferences.getBoolean(KeyData.KEY_NEWS_CHO,false)
        valueMap[KeyData.KEY_NEWS_KHAN] = sharedPreferences.getBoolean(KeyData.KEY_NEWS_KHAN,false)

        val trueList = arrayListOf<String>()
        valueMap.forEach { (key, value) ->
            if(value) trueList.add(key)
        }

        _newsSetComplete.value = trueList
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

