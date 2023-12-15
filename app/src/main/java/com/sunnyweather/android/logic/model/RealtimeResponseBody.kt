package com.sunnyweather.android.logic.model

/**
 * 请描述职责范围
 *
 * @since 2023/12/11
 */
data class RealtimeResponseBody(
    var status: String,
    var api_version: String,
    var api_status: String,
    val lang: String,
    val unit: String,
    var tzshift: Int,
    var timezone: String,
    var server_time: Long,
    var location: List<Double>,
    var result: Result
) {
    data class Wind(var speed: Float, var direction: Int)

    data class Local(var status: String, var datasource: String, var intensity: Double)

    data class Nearest(var status: String, var distance: Float, var intensity: Double)

    data class Precipitation(var local: Local, var nearest: Nearest)

    data class Aqi(var chn: Int, var usa: Int)

    data class Description(var chn: String, var usa: String)

    data class Air_quality(
        var pm25: Int,
        var pm10: Int,
        var o3: Int,
        var so2: Int,
        var no2: Int,
        var co: Float,
        var aqi: Aqi,
        var description: Description
    )

    data class Ultraviolet(var index: Int, var desc: String)

    data class Comfort(var index: Int, var desc: String)

    data class Life_index(var ultraviolet: Ultraviolet, var comfort: Comfort)

    data class Realtime(
        var status: String,
        var temperature: Float,
        var humidity: Float,
        var cloudrate: Float,
        var skycon: String,
        var visibility: Float,
        var dswrf: Float,
        var wind: Wind,
        var pressure: Float,
        var apparent_temperature: Float,
        var precipitation: Precipitation,
        var air_quality: Air_quality,
        var life_index: Life_index
    )

    data class Result(var realtime: Realtime, var primary: Int)
}
