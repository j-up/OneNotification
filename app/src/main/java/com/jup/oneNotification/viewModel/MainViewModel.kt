package com.jup.oneNotification.viewModel

import android.app.TimePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class MainViewModel: ViewModel() {
    private val _onTimeSetting = MutableLiveData<Boolean>()

    val onTimeSetting: LiveData<Boolean> get () = _onTimeSetting

    fun onTimeSetting() {
        /*TimePickerDialog(this, TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
            println(hour)
            println(minute)
        }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true).show()*/
        _onTimeSetting.value = true
    }
}