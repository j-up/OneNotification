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
            time_setting_text_view.text = it.timeToAmPm()
        })

        mainViewModel.weatherSetComplete.observe(this, Observer {
            when(it) {
                KeyData.VALUE_WEATHER_OPEN_WEATHER -> open_weather_radio_button.isChecked = true
                KeyData.VALUE_WEATHER_KOREA_WEATHER -> korea_weather_radio_button.isChecked = true
            }
        })

        mainViewModel.newsSetComplete.observe(this, Observer {
            val iterator = it.iterator()

            while(iterator.hasNext()) {
                when(iterator.next()) {
                    KeyData.KEY_NEWS_CHO -> cho_check_box.isChecked = true
                    KeyData.KEY_NEWS_KHAN -> khan_check_box.isChecked = true
                }
            }
        })

        mainViewModel.fashionSetComplete.observe(this, Observer {
            fashion_switch.isChecked = it
        })

        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this@MainActivity
    }
}
