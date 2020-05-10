package com.jup.oneNotification.view.dialog

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment() : DialogFragment() {
    var listener:TimePickerDialog.OnTimeSetListener? = null
    private val cal = Calendar.getInstance()

    companion object {
        fun newInstance(listener:TimePickerDialog.OnTimeSetListener?): TimePickerFragment {
            val fragment = TimePickerFragment()
            fragment.listener = listener
            return fragment
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = TimePickerDialog(
        context, listener, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), true
    )


}