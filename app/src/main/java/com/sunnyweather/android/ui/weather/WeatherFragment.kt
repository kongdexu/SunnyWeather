package com.sunnyweather.android.ui.weather

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.FragmentWeatherBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import com.sunnyweather.android.ui.base.BaseFragment
import com.sunnyweather.android.ui.place.PlaceFragment
import com.sunnyweather.android.util.ToastUtil
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * 天气详情
 *
 * @since 2023/12/12
 */
class WeatherFragment: BaseFragment() {

    private val TAG = "WeatherFragment"

    private val viewMode by lazy { ViewModelProvider(this)[WeatherViewModel::class.java] }

    private lateinit var dataBinding: FragmentWeatherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_weather, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (viewMode.locationLng.isEmpty()) {
            viewMode.locationLng = arguments?.getString("location_lng") ?: ""
        }
        if (viewMode.locationLat.isEmpty()) {
            viewMode.locationLat = arguments?.getString("location_lat") ?: ""
        }
        if (viewMode.placeName.isEmpty()) {
            viewMode.placeName = arguments?.getString("place_name") ?: ""
        }
        viewMode.weatherLiveData.observe(viewLifecycleOwner, Observer { result ->
            val weather = result.getOrNull()
            if (weather != null) {
                showWeatherInfo(weather)
            } else {
                ToastUtil.showToast("无法获取天气信息")
                result.exceptionOrNull()?.printStackTrace()
            }
            dataBinding.swipeRefresh.isRefreshing = false
        })
        dataBinding.swipeRefresh.setOnRefreshListener { refreshWeather() }
        dataBinding.nowLayout.imageSwitch.setOnClickListener { pushPlaceFragment() }
        refreshWeather()
    }

    private fun refreshWeather() {
        Log.i(TAG, "refreshWeather lat = ${viewMode.locationLat} ---- lon = ${viewMode.locationLng}")
        viewMode.refreshWeather(viewMode.locationLng, viewMode.locationLat)
        dataBinding.swipeRefresh.isRefreshing = true
    }

    private fun showWeatherInfo(weather: Weather) {
        Log.i(TAG, "showWeatherInfo $weather")
        dataBinding.nowLayout.placeName.text = viewMode.placeName
        val realTime = weather.realTime
        val daily = weather.daily
        val currentTempText = "${realTime.temperature} ℃"
        dataBinding.nowLayout.currentTemp.text = currentTempText
        dataBinding.nowLayout.currentSky.text = getSky(realTime.skycon).info
        val currentPM25Text = "空气指数 ${realTime.air_quality.aqi.chn}"
        dataBinding.nowLayout.currentAQI.text = currentPM25Text
        dataBinding.nowLayout.nowLayout.setBackgroundResource(getSky(realTime.skycon).bg)
        dataBinding.forecastLayout.forecastLayout.removeAllViews()
        val days = daily.skycon.size
        for (i in 0 until days) {
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
            val view = LayoutInflater.from(context).inflate(R.layout.forecast_item, dataBinding.forecastLayout.forecastLayout, false)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val dateInfo = view.findViewById(R.id.dateInfo) as TextView
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById(R.id.temperatureInfo) as TextView
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
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

    private fun pushPlaceFragment() {
        //var placeFragment = findFragmentByTag(PlaceFragment::class.java.name) as? PlaceFragment
        var placeFragment: PlaceFragment? = null
        if (placeFragment == null) {
            Log.i(TAG, "pushPlaceFragment placeFragment == null")
            placeFragment = PlaceFragment()
        }
        placeFragment.setIsRefresh(false)
        showFragment(placeFragment)
    }
/*
    private lateinit var locationManager: LocationManager
    private fun getLocation() {
        Log.i(TAG, "getLocation SDK_INT = ${Build.VERSION.SDK_INT}")
        locationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 判断当前是否拥有使用GPS的权限
        if (ActivityCompat.checkSelfPermission(
                SunnyWeatherApplication.context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                SunnyWeatherApplication.context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 申请权限
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ),
                    100
                )
            }
            return
        }
        // 启动位置请求
        // LocationManager.GPS_PROVIDER GPS定位
        // LocationManager.NETWORK_PROVIDER 网络定位
        // LocationManager.PASSIVE_PROVIDER 被动接受定位信息
        val provider = locationManager.getProviders(true)
        if (provider.contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, this)
        } else if (provider.contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, this)
        } else if (provider.contains(LocationManager.PASSIVE_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0f, this)
        } else {
            ToastUtil.showToast("定位服务不可用")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.i(TAG, "onRequestPermissionsResult requestCode = $requestCode")
        Log.i(TAG, "onRequestPermissionsResult permissions = $permissions")
        Log.i(TAG, "onRequestPermissionsResult grantResults = $grantResults")
        if (requestCode == 100 && grantResults.isNotEmpty()) {
            var permission = true
            for (i in grantResults) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    permission = false
                }
            }
            if (permission) {
                getLocation()
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        viewMode.locationLat = location.latitude.toString()
        viewMode.locationLng = location.longitude.toString()
        // 移除位置管理器
        // 需要一直获取位置信息可以去掉这个
        locationManager.removeUpdates(this)
        refreshWeather()
    }*/
}