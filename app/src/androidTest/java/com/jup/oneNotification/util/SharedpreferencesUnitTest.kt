package com.jup.oneNotification.util

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.jup.oneNotification.utils.JLog
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedpreferencesUnitTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val TEST_KEY = "TEST_KEY"
    private val TEST_VALUE = "this test mode"
    lateinit var sharedPref:SharedPreferences


    @Before
    fun beforeTest() {
        Assert.assertNotNull("context is null",context)
    }

    @Test
    fun mainTest() {
        JLog.d(this::class.java,context.packageName)
        sharedPref = context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putString(TEST_KEY,TEST_VALUE).commit()
        }

        Assert.assertEquals("not equals",sharedPref.getString(TEST_KEY," "),TEST_VALUE)
    }

    @After
    fun afterTest() {

    }
}