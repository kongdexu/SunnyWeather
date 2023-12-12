package com.sunnyweather.android.logic.model

/**
 * 请描述职责范围
 *
 * @since 2023/12/8
 */
data class Weather(val realTime: RealtimeResponseBody.Realtime, val daily: DailyResponse.Daily)