
package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.data.model.RouteStop
import com.example.arch_izmir.databinding.ItemRouteBinding

class RouteAdapter(
    private var stopList: List<RouteStop>,
    private val onDeleteClick: (RouteStop) -> Unit,
    private val onStartDrag: (RecyclerView.ViewHolder) -> Unit
) : RecyclerView.Adapter<RouteAdapter.RouteViewHolder>() {

    inner class RouteViewHolder(
        private val binding: ItemRouteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stop: RouteStop, position: Int) {

            binding.tvStopNumber.text = "${position + 1}"
            binding.tvPlaceTitle.text = stop.title
            binding.tvPlaceSubtitle.text = stop.location
            binding.tvVisitDuration.text = stop.visitDuration

            binding.btnDelete.setOnClickListener {
                onDeleteClick(stop)
            }

            // Sadece tutamaç basılı tutulduğunda sürükle.
            binding.ivDragHandle.setOnLongClickListener {
                onStartDrag(this)
                true
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RouteViewHolder {

        val binding = ItemRouteBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return RouteViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RouteViewHolder,
        position: Int
    ) {
        holder.bind(stopList[position], position)
    }

    // Veritabanından yeni liste geldiğinde çağrılır.
    fun updateList(newList: List<RouteStop>) {
        stopList = newList
        notifyDataSetChanged()
    }

    // Kartların sırasını değiştirir.
    fun moveItem(fromPosition: Int, toPosition: Int) {

        val newList = stopList.toMutableList()

        val movedItem = newList.removeAt(fromPosition)

        newList.add(toPosition, movedItem)

        stopList = newList

        notifyItemMoved(fromPosition, toPosition)

        // Kartların üzerindeki sıra numaralarını yenile.
        notifyItemRangeChanged(
            minOf(fromPosition, toPosition),
            kotlin.math.abs(toPosition - fromPosition) + 1
        )
    }

    // Güncel listeyi Fragment'a verir.
    fun getCurrentStops(): List<RouteStop> {
        return stopList
    }

    override fun getItemCount(): Int {
        return stopList.size
    }
}
