package com.sunnyweather.android.logic

import androidx.lifecycle.liveData
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.logic.network.SunnyWeatherNetwork
import kotlinx.coroutines.Dispatchers


/**
 * 请描述职责范围
 *
 * @since 2023/12/8
 */
object Repository {

  fun searchPlaces(query: String) = liveData(Dispatchers.IO) {
      val result = try {
          val placeResponse = SunnyWeatherNetwork.searchPlaces(query)
          if (placeResponse.status == "ok") {
              val places = placeResponse.places
              Result.success(places)
          } else {
              Result.failure(RuntimeException("response status is ${placeResponse.status}"))
          }
      } catch (exception: Exception) {
          Result.failure(exception)
      }
      emit(result)
  }

}