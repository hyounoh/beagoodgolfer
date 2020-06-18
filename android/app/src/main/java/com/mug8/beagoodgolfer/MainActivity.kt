package com.mug8.beagoodgolfer

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.mug8.beagoodgolfer.constant.TAG

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun analyze(view: View) {
        Log.d(TAG, "analyze")
    }

    fun reset(view: View) {
        Log.d(TAG, "reset")
    }
}