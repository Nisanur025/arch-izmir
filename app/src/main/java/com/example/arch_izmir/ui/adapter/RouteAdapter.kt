package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.data.model.RouteStop
import com.example.arch_izmir.databinding.ItemRouteBinding

class RouteAdapter(
    private var stopList : List<RouteStop>,
    private val onDeleteClick : (RouteStop) -> Unit
): RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {
    inner class RouteViewHolder(private val binding: ItemRouteBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(stop: RouteStop, position: Int){
            binding.tvStopNumber.text ="${position+1}"
            binding.tvPlaceTitle.text =stop.title
            binding.tvPlaceSubtitle.text = stop.location
            binding.tvVisitDuration.text = stop.visitDuration
            binding.btnDelete.setOnClickListener { onDeleteClick(stop) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        val binding = ItemRouteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.bind(stopList[position],position)
    }

    fun updateList(newList :List<RouteStop>){
        stopList = newList
        notifyDataSetChanged()

    }
    override fun getItemCount(): Int {
        return stopList.size
    }
}