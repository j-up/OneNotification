package com.jup.oneNotification.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    private val _onTimeSetting = MutableLiveData<Boolean>()

    val onTimeSetting: LiveData<Boolean> get () = _onTimeSetting

    fun onTimeSetting() {
        _onTimeSetting.value = true
    }
}