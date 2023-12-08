package com.sunnyweather.android.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.ActivityWeatherBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.util.ToastUtil
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherActivity : AppCompatActivity() {

    private val viewMode by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    private lateinit var dataBinding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_weather)
        if (viewMode.locationLng.isEmpty()) {
            viewMode.locationLat = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewMode.locationLat.isEmpty()) {
            viewMode.locationLat = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewMode.placeName.isEmpty()) {
            viewMode.placeName = intent.getStringExtra("placeName") ?: ""
        }
/*        viewMode.weatherLiveData.observe(this, Observer { result ->
            val weather = result.get
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                ToastUtil.showToast("无法获取天气信息")
                result.exceptionOrNull()?.printStackTrace()
            }
        })*/

        viewMode.weatherLiveData.observe(this, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                ToastUtil.showToast("无法获取天气信息")
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }

    private fun showWeatherInfo(weather: Weather) {
        dataBinding.nowLayout.placeName.text = viewMode.placeName
        val realTime = weather.realTime
        val daily = weather.daily
        val currentTempText = "${realTime.temperature.toInt()} ℃"
        dataBinding.nowLayout.currentTemp.text = currentTempText
        //dataBinding.nowLayout.currentSky.text = getSky(realtime.skycon).info
        val currentPM25Text = "空气指数 ${realTime.airQuality.chn.toInt()}"
        dataBinding.nowLayout.currentAQI.text = currentPM25Text
        //dataBinding.nowLayout.nowLayout.setBackgroundResource(getSky(realtime.skycon).bg)
        dataBinding.forecastLayout.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item, dataBinding.forecastLayout.forecastLayout, false)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            dateInfo.text = simpleDateFormat.format(skycon.date)
            //val sky = getSky(skycon.value)
            //skyIcon.setImageResource(sky.icon)
            //skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()} ~ ${temperature.max.toInt()} ℃"
            temperatureInfo.text = tempText
            dataBinding.forecastLayout.forecastLayout.addView(view)
        }
        // 填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        dataBinding.lifeIndexLayout.coldRiskText.text = lifeIndex.coldRisk[0].desc
        dataBinding.lifeIndexLayout.dressingText.text = lifeIndex.dressing[0].desc
        dataBinding.lifeIndexLayout.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        dataBinding.lifeIndexLayout.carWashingText.text = lifeIndex.carWashing[0].desc
        dataBinding.weatherLayout.visibility = View.VISIBLE
    }
}

