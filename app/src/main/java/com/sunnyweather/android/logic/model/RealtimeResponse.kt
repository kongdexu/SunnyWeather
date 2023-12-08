package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName

/**
 * 实时天气信息数据
 *
 * @since 2023/12/8
 */
data class RealtimeResponse (val status: String, val result: Result){

    data class Result(val realTime: RealTime)

    data class RealTime(val skycon: String, val temperature: Float, @SerializedName("air_quality") val airQuality: AirQuality)

    data class AirQuality(val chn: Float)
}