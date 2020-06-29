package com.jup.oneNotification.view

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jup.oneNotification.R
import com.jup.oneNotification.core.provider.AddressProvider
import com.jup.oneNotification.databinding.ActivityMainBinding
import com.jup.oneNotification.model.KeyData
import com.jup.oneNotification.utils.RequestPermission
import com.jup.oneNotification.view.dialog.TimePickerFragment
import com.jup.oneNotification.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {
    private val requestCode = 1001
    private val permissions = arrayListOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)

    private val timePickerFragment: TimePickerFragment by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val addressProvider: AddressProvider by inject{ parametersOf(applicationContext) }
    private val requestPermission: RequestPermission by inject{ parametersOf(permissions,applicationContext)  }

    private val mainViewModel: MainViewModel by inject{ parametersOf(timePickerFragment,sharedPreferences,addressProvider,requestPermission) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

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
