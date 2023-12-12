package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.sunnyweather.R
import com.sunnyweather.android.logic.model.Place
import com.sunnyweather.android.ui.weather.WeatherFragment

/**
 * 请描述职责范围
 *
 * @since 2023/12/8
 */
class PlaceAdapter(private val fragment: Fragment, private val placeList: List<Place>) :
    RecyclerView.Adapter<PlaceAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeName)
        val placeAddress: TextView = view.findViewById(R.id.placeAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(fragment.context).inflate(R.layout.place_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = placeList[position]
        holder.placeName.text = place.name
        holder.placeAddress.text = place.address
        holder.itemView.setOnClickListener{
            val weatherFragment = WeatherFragment()
            val bundle = Bundle().apply {
                putString("location_lng", place.location.lng)
                putString("location_lat", place.location.lat)
                putString("place_name", place.name)
            }
            weatherFragment.arguments = bundle
            fragment.activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.place_fragment, weatherFragment)
                ?.addToBackStack(null)
                ?.commit()
        }
    }

    override fun getItemCount() = placeList.size
}