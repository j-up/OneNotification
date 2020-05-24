package com.jup.oneNotification.view

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jup.oneNotification.R
import com.jup.oneNotification.databinding.ActivityMainBinding
import com.jup.oneNotification.model.KeyData
import com.jup.oneNotification.utils.JLog
import com.jup.oneNotification.view.dialog.TimePickerFragment
import com.jup.oneNotification.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val sharedPreferences: SharedPreferences by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val timePickerFragment = TimePickerFragment.newInstance(null,sharedPreferences)
        val mainViewModel = MainViewModel(timePickerFragment,sharedPreferences)

        mainViewModel.onTimeClickListener.observe(this, Observer {
            it.show(supportFragmentManager,"TIME_PICKER")
        })

        mainViewModel.timeSetComplete.observe(this, Observer {
            JLog.d(this::class.java,"Set Time - ${it.toString()}")
            time_setting_text_view.text = it.toString()
        })

        mainViewModel.weatherSetComplete.observe(this, Observer {
            JLog.d(this::class.java,"Set Weather - ${it.toString()}")
            when(it) {
                KeyData.VALUE_WEATHER_OPEN_WEATHER -> open_weather_radio_button.isChecked = true
                KeyData.VALUE_WEATHER_KOREA_WEATHER -> korea_weather_radio_button.isChecked = true
            }
        })

        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this@MainActivity
    }
}
