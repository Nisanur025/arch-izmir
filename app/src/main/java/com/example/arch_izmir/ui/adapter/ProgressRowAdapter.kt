package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.ProgressRow
import com.google.android.material.progressindicator.LinearProgressIndicator

class ProgressRowAdapter(
    private val items: List<ProgressRow>
) : RecyclerView.Adapter<ProgressRowAdapter.VH>() {
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val label: TextView = v.findViewById(R.id.rowLabel)
        val count: TextView = v.findViewById(R.id.rowCount)
        val progress: LinearProgressIndicator = v.findViewById(R.id.rowProgress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_progress_row, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val item = items[position]
        val ctx = h.itemView.context
        h.label.text = item.label
        h.count.text = item.countText
        h.progress.max = item.max
        h.progress.setProgressCompat(item.value, true)

        // Gezilmiş olanlar altın, gezilmemişler normal renkte
        val color = if (item.value > 0) R.color.passport_gold else R.color.passport_text
        h.label.setTextColor(ContextCompat.getColor(ctx, color))
    }
}