package com.jup.oneNotification.view

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jup.oneNotification.R
import com.jup.oneNotification.core.provider.LocationProvider
import com.jup.oneNotification.databinding.ActivityMainBinding
import com.jup.oneNotification.core.common.KeyData
<<<<<<< HEAD
import com.jup.oneNotification.utils.RequestPermission
=======
import com.jup.oneNotification.utils.JLog
>>>>>>> c7094647381583b99a2bdf3718aa68abce978301
import com.jup.oneNotification.view.dialog.TimePickerFragment
import com.jup.oneNotification.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1001

    private val timePickerFragment: TimePickerFragment by inject()
    private val sharedPreferences: SharedPreferences by inject()
    private val locationProvider: LocationProvider by inject()
    private val requestPermission: RequestPermission by inject{ parametersOf(applicationContext)  }

    private lateinit var mainViewModel: MainViewModel
    private lateinit var requestPermissionArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        mainViewModel = MainViewModel(timePickerFragment,sharedPreferences,locationProvider,requestPermission)

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

        mainViewModel.permissionCheck.observe(this, Observer {
            requestPermissionArray = it.toTypedArray()
            ActivityCompat.requestPermissions(this, requestPermissionArray, PERMISSION_REQUEST_CODE)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val deniedArrayList = ArrayList<String>()
        when(requestCode) {
            PERMISSION_REQUEST_CODE -> {
                for(i in grantResults.indices) {
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        deniedArrayList.add(requestPermissionArray[i])
                    }
                }
            }
        }

        if(deniedArrayList.isNotEmpty()) {
            for(i in deniedArrayList.indices) {
                if (shouldShowRequestPermissionRationale(deniedArrayList[i])) {
                    requestPermissions(
                        arrayOf(deniedArrayList[i]),
                        PERMISSION_REQUEST_CODE)
                } else {
                    showDialogToGetPermission()
                }
            }
        }
        /*var checkResult = true;
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==permissionRequestCode && grantResults.size == requestPermissionArray.size) {
            for(item in grantResults) {
                if(item != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false;
                    break;
                }
            }

            if(!checkResult) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, requestPermissionArray[0])) {

                }
            }
        }*/
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Permisisons request")
            .setMessage("We need the location permission for some reason. " +
                    "You need to move on Settings to grant some permissions")

        builder.setPositiveButton("OK") { dialogInterface, i ->
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)   // 6
        }
        builder.setNegativeButton("Later") { dialogInterface, i ->

        }
        val dialog = builder.create()
        dialog.show()
    }
}
