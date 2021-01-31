package com.jup.oneNotification.view

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.jup.oneNotification.R
import com.jup.oneNotification.databinding.ActivityMainBinding
import com.jup.oneNotification.core.common.KeyData
import com.jup.oneNotification.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUEST_CODE = 1001

    private val mainViewModel by viewModel<MainViewModel>()
    private lateinit var requestPermissionArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        mainViewModel.onTimeClickListener.observe(this, Observer {
            it?.let {
                it.show(supportFragmentManager,"TIME_PICKER")
            }
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

        mainViewModel.fashionSetComplete.observe(this, Observer {
            fashion_switch.isChecked = it
        })

        mainViewModel.locationSetComplete.observe(this, Observer {
            location_init_ly.setBackgroundColor(Color.parseColor("#1DDB16"))
            address_text_view.text = it
        })
        
        mainViewModel.onError.observe(this, Observer {
            Toast.makeText(applicationContext, it,Toast.LENGTH_SHORT).show()
        })

        binding.mainViewModel=mainViewModel
        binding.lifecycleOwner=this@MainActivity
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
            .setTitle("권한 요청")
            .setMessage("앱 사용을 위해선 위치 승인이 필요합니다. 설정으로 이동하시겠습니까?")
            .setPositiveButton("이동") { _, _ ->
            val intent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            }
            .setNegativeButton("나중에") { _, _ ->
            return@setNegativeButton
        }
        val dialog = builder.create()
        dialog.show()
    }
}
