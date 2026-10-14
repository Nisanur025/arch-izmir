package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.data.model.Place
import com.example.arch_izmir.databinding.RvItemNearbyPlaceBinding

class NearbyPlacesAdapter(
    private val places: List<Place>,
    private val onItemClick: (Place) -> Unit
) : RecyclerView.Adapter<NearbyPlacesAdapter.NearbyViewHolder>() {

    class NearbyViewHolder(val binding: RvItemNearbyPlaceBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyViewHolder {
        val binding = RvItemNearbyPlaceBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return NearbyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyViewHolder, position: Int) {
        val place = places[position]
        holder.binding.tvName.text = place.name
        holder.binding.tvDistance.text = "${place.distanceKm} km"
        holder.binding.root.setOnClickListener { onItemClick(place) }
    }

    override fun getItemCount() = places.size
}