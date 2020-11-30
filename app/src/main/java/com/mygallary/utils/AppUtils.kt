package com.mygallary.utils

import androidx.appcompat.app.AppCompatActivity
import android.view.inputmethod.InputMethodManager

object AppUtils {

    fun hideSoftKeyboard(activity: AppCompatActivity) {
        val inputMethodManager: InputMethodManager =
            activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()?.getWindowToken(), 0)
    }

}