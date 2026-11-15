package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.Badge
import com.example.arch_izmir.data.model.BadgeState
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator

class BadgeAdapter(private val items: List<Badge>) :
    RecyclerView.Adapter<BadgeAdapter.VH>() {

    //     kartın içindeki viewları bulup saklar
    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val card = v as MaterialCardView
        val icon: ImageView = v.findViewById(R.id.badgeIcon)
        val name: TextView = v.findViewById(R.id.badgeName)
        val desc: TextView = v.findViewById(R.id.badgeDesc)
        val progress: LinearProgressIndicator = v.findViewById(R.id.badgeProgress)
        val info: TextView = v.findViewById(R.id.badgeInfo)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_badge, parent, false)
    )

    override fun getItemCount() = items.size

    override fun onBindViewHolder(h: VH, position: Int) {
        val badge = items[position]
        val ctx = h.itemView.context
        val gold = ContextCompat.getColor(ctx, R.color.passport_gold)
        val soft = ContextCompat.getColor(ctx, R.color.passport_text_soft)
        val track = ContextCompat.getColor(ctx, R.color.passport_track)

        // Her durumda aynı olan kısım
        h.name.text = badge.name
        h.desc.text = badge.description

        when (badge.state) {
            BadgeState.EARNED -> {
                h.icon.setColorFilter(gold)
                h.card.strokeColor = gold
                h.progress.visibility = View.GONE
                h.info.text = badge.dateText
                h.info.setTextColor(gold)
                h.itemView.alpha = 1f
            }
            BadgeState.IN_PROGRESS -> {
                h.icon.setColorFilter(soft)
                h.card.strokeColor = track
                h.progress.visibility = View.VISIBLE
                h.progress.max = badge.max
                h.progress.setProgressCompat(badge.value, false)
                h.info.text = "${badge.value} / ${badge.max}"
                h.info.setTextColor(soft)
                h.itemView.alpha = 1f
            }
            BadgeState.LOCKED -> {
                h.icon.setColorFilter(soft)
                h.card.strokeColor = track
                h.progress.visibility = View.GONE
                h.info.text = "${badge.value} / ${badge.max}"
                h.info.setTextColor(soft)
                h.itemView.alpha = 0.6f
            }
        }
    }
}