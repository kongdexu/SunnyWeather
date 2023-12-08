package com.sunnyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

/**
 * 全局Application
 *
 * @since 2023/12/7
 */
class SunnyWeatherApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        /**
         * 天气接口令牌
         */
        const val TOKEN = "CQG2puMBaiOs9sT0"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}