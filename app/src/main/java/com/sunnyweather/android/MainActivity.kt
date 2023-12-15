package com.sunnyweather.android

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.FragmentActivity
import com.example.sunnyweather.R
import com.sunnyweather.android.ui.base.BaseFragment


class MainActivity : FragmentActivity() {

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
//        //注意要清除 FLAG_TRANSLUCENT_STATUS flag
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
//       // window.statusBarColor = resources.getColor(android.R.color.holo_red_light)
        setContentView(R.layout.activity_main)
        BaseFragment.mContainerViewId = R.id.weather_fragment
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.i(TAG, "onBackPressed")
        if (supportFragmentManager.fragments.isEmpty()) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
