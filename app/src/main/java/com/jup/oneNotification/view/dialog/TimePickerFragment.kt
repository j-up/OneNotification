package com.jup.oneNotification.view.dialog

import android.app.TimePickerDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.jup.oneNotification.model.KeyData
import java.util.*

class TimePickerFragment() : DialogFragment() {
    var listener:TimePickerDialog.OnTimeSetListener? = null
    lateinit var sharedPreferences:SharedPreferences
    private val cal = Calendar.getInstance()

    companion object {
        fun newInstance(listener:TimePickerDialog.OnTimeSetListener?,sharedPreferences: SharedPreferences): TimePickerFragment {
            val fragment = TimePickerFragment()
            fragment.listener = listener
            fragment.sharedPreferences = sharedPreferences
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = TimePickerDialog(
        context, listener, sharedPreferences.getInt(KeyData.TIME_HOUR,cal.get(Calendar.HOUR)),  sharedPreferences.getInt(KeyData.TIME_MINUTE,cal.get(Calendar.MINUTE)), true
    )

}