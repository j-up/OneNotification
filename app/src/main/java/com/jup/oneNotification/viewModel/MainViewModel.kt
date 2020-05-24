package com.jup.oneNotification.viewModel

import android.app.Application
import android.app.TimePickerDialog
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jup.oneNotification.model.AlarmDate
import com.jup.oneNotification.model.KeyData
import com.jup.oneNotification.view.dialog.TimePickerFragment
import java.util.*

class MainViewModel(private val timePickerDialog: TimePickerFragment, private val sharedPreferences: SharedPreferences): ViewModel() {
    private val _timeSetComplete = MutableLiveData<AlarmDate>()
    private val _onTimeClickListener = MutableLiveData<TimePickerFragment>()
    private val cal = Calendar.getInstance()

    val timeSetComplete: LiveData<AlarmDate> get () = _timeSetComplete
    val onTimeClickListener: LiveData<TimePickerFragment>  get () = _onTimeClickListener

    init{
        val hour = sharedPreferences.getInt(KeyData.TIME_HOUR, 0)
        val minute = sharedPreferences.getInt(KeyData.TIME_HOUR, 0)

        // 값이 없을때 현재 시간으로 초기화
        if (hour==0 || minute ==0) {
            with(sharedPreferences.edit()) {
                putInt(KeyData.TIME_HOUR, cal.get(Calendar.HOUR)).commit()
                putInt(KeyData.TIME_MINUTE, cal.get(Calendar.MINUTE)).commit()
            }
        }

        AlarmDate(
            sharedPreferences.getInt(KeyData.TIME_HOUR, 0),
            sharedPreferences.getInt(KeyData.TIME_MINUTE, 0)
        ).run {
            _timeSetComplete.value = this
        }
    }

    fun onTimeClick() {
        timePickerDialog.listener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            _timeSetComplete.value = AlarmDate(hour,minute)

            with(sharedPreferences.edit()) {
                putInt(KeyData.TIME_HOUR,hour).commit()
                putInt(KeyData.TIME_MINUTE,minute).commit()
            }
        }
        _onTimeClickListener.value = timePickerDialog
    }
}

