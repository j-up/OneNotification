package com.jup.oneNotification.viewModel

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.view.View
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jup.oneNotification.R
import com.jup.oneNotification.model.AlarmDate
import com.jup.oneNotification.model.KeyData
import com.jup.oneNotification.utils.JLog
import com.jup.oneNotification.view.dialog.TimePickerFragment
import java.util.*

class MainViewModel(private val timePickerDialog: TimePickerFragment, private val sharedPreferences: SharedPreferences): ViewModel() {
    private val cal = Calendar.getInstance()
    private val _timeSetComplete = MutableLiveData<AlarmDate>()
    private val _onTimeClickListener = MutableLiveData<TimePickerFragment>()
    private val _weatherSetComplete = MutableLiveData<Int>()
    private val _newsSetComplete = MutableLiveData<ArrayList<String>>()

    val timeSetComplete: LiveData<AlarmDate> get () = _timeSetComplete
    val onTimeClickListener: LiveData<TimePickerFragment>  get () = _onTimeClickListener
    val weatherSetComplete: LiveData<Int> get () = _weatherSetComplete
    val newsSetComplete: LiveData<ArrayList<String>> get () =_newsSetComplete

    init{
        initTime()
        initWeather()
        initNews()
    }

    fun onTimeClick() {
        timePickerDialog.listener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            _timeSetComplete.value = AlarmDate(hour,minute)
            JLog.d(this::class.java,"Set Time - ${_timeSetComplete.value.toString()}")

            with(sharedPreferences.edit()) {
                putInt(KeyData.KEY_TIME_HOUR,hour).commit()
                putInt(KeyData.KEY_TIME_MINUTE,minute).commit()
            }
        }
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
        ).run {
            _timeSetComplete.value = this
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
}

