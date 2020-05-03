package com.jup.oneNotification.view

import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.jup.oneNotification.R
import com.jup.oneNotification.databinding.ActivityMainBinding
import com.jup.oneNotification.utils.JLog
import com.jup.oneNotification.viewModel.MainViewModel


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        val mainViewModel = MainViewModel()

        val timeDialog = DialogFragment()

        val callbackMethod =
            OnTimeSetListener { view, hourOfDay, minute ->
                //setText(hourOfDay.toString() + "시" + minute + "분")
            }



        mainViewModel.onTimeSetting.observe(this, Observer {
            JLog.d(this,"s")
        })

        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this@MainActivity

        setContentView(R.layout.activity_main)
    }
}
