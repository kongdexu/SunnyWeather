package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sunnyweather.R
import com.example.sunnyweather.databinding.FragmentPlaceBinding
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.base.BaseFragment
import com.sunnyweather.android.ui.weather.WeatherFragment
import com.sunnyweather.android.util.ToastUtil

/**
 * 请描述职责范围
 *
 * @since 2023/12/8
 */
class PlaceFragment: BaseFragment() {

    private val TAG = "PlaceFragment"

    val viewModel by lazy { ViewModelProvider(this)[PlaceViewModel::class.java] }

    private lateinit var placeAdapter: PlaceAdapter

    private lateinit var dataBinding: FragmentPlaceBinding

    private var isRefresh = true;

    fun setIsRefresh(b: Boolean) {
        this.isRefresh = b
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dataBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_place, container, false)
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (Repository.isSavePlace() && isRefresh) {
            val place = Repository.getSavePlace()
            pushWeatherFragment(place)
            return
        }
        val layoutManager = LinearLayoutManager(activity)
        dataBinding.recyclerView.layoutManager = layoutManager
        placeAdapter = PlaceAdapter(this, viewModel.placeList)
        dataBinding.recyclerView.adapter = placeAdapter
        dataBinding.searchPlaceEdit.addTextChangedListener {
            val content = it.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                dataBinding.recyclerView.visibility = View.GONE
                viewModel.placeList.clear()
                placeAdapter.notifyDataSetChanged()
            }
        }
        viewModel.placeLiveData.observe(viewLifecycleOwner) {
            val places = it.getOrNull()
            Log.d(TAG, "observe places = $places")
            if (places != null) {
                dataBinding.recyclerView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                placeAdapter.notifyDataSetChanged()
            } else {
                ToastUtil.showToast("未能查询到任何地点")
                it.exceptionOrNull()?.printStackTrace()
            }
        }
    }

    fun pushWeatherFragment(place: Place) {
        Log.i(TAG, "pushWeatherFragment place: $place")
        //var weatherFragment = findFragmentByTag(WeatherFragment::class.java.name)
        var weatherFragment: WeatherFragment? = null
        if (weatherFragment == null) {
            Log.i(TAG, "pushWeatherFragment weatherFragment == null")
            weatherFragment = WeatherFragment()
        }
        val bundle = Bundle().apply {
            putString("location_lng", place.location.lng)
            putString("location_lat", place.location.lat)
            putString("place_name", place.name)
        }
        weatherFragment.arguments = bundle
        showFragment(weatherFragment)
    }

}