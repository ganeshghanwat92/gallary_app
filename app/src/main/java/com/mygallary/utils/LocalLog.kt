package com.mygallary.utils

import android.util.Log
import com.mygallary.BuildConfig


/**
 * Created by Ganesh on 30/11/20.
 * ganeshghanwat92@gmail.com
 */
object LocalLog {

    fun d(tag: String, msg : String){
        if (BuildConfig.SHOULD_LOG){
            Log.d(tag, msg)
        }
    }

    fun e(tag: String, msg : String){
        if (BuildConfig.SHOULD_LOG){
            Log.e(tag, msg)
        }
    }

}