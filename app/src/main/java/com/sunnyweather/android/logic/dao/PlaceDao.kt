package com.sunnyweather.android.logic.dao

import android.content.Context
import com.google.gson.Gson
import com.sunnyweather.android.SunnyWeatherApplication
import com.sunnyweather.android.logic.model.Place

/**
 * 请描述职责范围
 *
 * @since 2023/12/13
 */
object PlaceDao {

    private fun getSharedPreferences() = SunnyWeatherApplication.context.getSharedPreferences("sunny_weather", Context.MODE_PRIVATE)

    fun savePlace(place: Place) {
        getSharedPreferences().edit().putString("place", Gson().toJson(place)).apply()
    }

    fun getSavePlace(): Place{
        val place = getSharedPreferences().getString("place", "")
        return Gson().fromJson(place, Place::class.java);
    }

    fun isSaved(): Boolean {
        return getSharedPreferences().contains("place")
    }
}