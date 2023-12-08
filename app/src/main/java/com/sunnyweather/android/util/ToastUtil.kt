package com.sunnyweather.android.util

import android.widget.Toast
import com.sunnyweather.android.SunnyWeatherApplication

/**
 * 请描述职责范围
 *
 * @since 2023/12/8
 */
object ToastUtil {

    /**
     * 弹toast
     */
    fun showToast(content: String) {
        Toast.makeText(SunnyWeatherApplication.context, content, Toast.LENGTH_SHORT).show()
    }
}