package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.data.model.Place
import com.example.arch_izmir.databinding.RvItemPassportPlacesBinding

class VisitedPlaceAdapter(
    private val places: List<Place>,
    private val onItemClick: (Place) -> Unit
): RecyclerView.Adapter<VisitedPlaceAdapter.VisitedViewHolder>() {
    class VisitedViewHolder(val binding: RvItemPassportPlacesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitedViewHolder {
        val binding = RvItemPassportPlacesBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return VisitedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VisitedViewHolder, position: Int) {
        val place = places[position]

        holder.binding.ivPlace.setImageResource(place.imageRes)
        holder.binding.tvPlaceName.text = place.name
        holder.binding.tvPoints.text = "+${place.points}"

        holder.itemView.setOnClickListener { onItemClick(place) }
    }

    override fun getItemCount(): Int = places.size
}