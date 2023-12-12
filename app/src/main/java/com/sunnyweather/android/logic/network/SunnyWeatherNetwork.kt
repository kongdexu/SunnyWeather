package com.sunnyweather.android.logic.network

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 网络数据源访问入口
 *
 * @since 2023/12/7
 */
object SunnyWeatherNetwork {

    private val TAG = "SunnyWeatherNetwork"

    private val placeService = ServiceCreator.create<PlaceService>()

    private val weatherService = ServiceCreator.create<WeatherService>()


    suspend fun searchPlaces(query: String) = placeService.searchPlaces(query).await()

    suspend fun getDailyWeather(lng: String, lat: String) = weatherService.getDailyWeather(lng, lat).await()

    suspend fun getRealTimeWeather(lng: String, lat: String) = weatherService.getRealtimeWeather(lng, lat).await()

    private suspend fun <T> Call<T>.await(): T{
        return suspendCoroutine {continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    Log.i(TAG, "onResponse isSuccessful: ${response.isSuccessful}")
                    Log.i(TAG, "onResponse message: ${response.raw()}")
                    Log.i(TAG, "onResponse info: ${response.body()}")
                    val body = response.body()
                    if (body != null) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(RuntimeException("response body is null"))
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    Log.i(TAG, "onFailure: ${t.message}")
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}