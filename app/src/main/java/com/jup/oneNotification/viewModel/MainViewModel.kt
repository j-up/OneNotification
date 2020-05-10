package com.jup.oneNotification.viewModel

import android.app.TimePickerDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jup.oneNotification.model.AlarmDate
import com.jup.oneNotification.view.dialog.TimePickerFragment

class MainViewModel(private val timePickerDialog: TimePickerFragment): ViewModel() {
    private val _timeSetComplete = MutableLiveData<AlarmDate>()
    private val _onTimeClickListener = MutableLiveData<TimePickerFragment>()

    val timeSetComplete: LiveData<AlarmDate> get () = _timeSetComplete
    val onTimeClickListener :LiveData<TimePickerFragment>  get () = _onTimeClickListener

    fun onTimeClick() {
        timePickerDialog.listener = TimePickerDialog.OnTimeSetListener{ _, hour, minute ->
            _timeSetComplete.value = AlarmDate(hour,minute)
        }
        _onTimeClickListener.value = timePickerDialog
    }
}

