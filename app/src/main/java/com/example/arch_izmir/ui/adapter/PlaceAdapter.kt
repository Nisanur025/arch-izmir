package com.example.arch_izmir.ui.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.Place
import com.example.arch_izmir.databinding.RvItemHomeBinding

class PlaceAdapter(
    private val placeList: List<Place>, // listede gösterilecek mekanlar
    private val onAddClick: (Place) -> Unit, // Rotaya ekleme
    private val onFavoriteClick:(Place) ->Unit, // Favoriye Ekleme
    private val onItemClick:(Place) -> Unit // Kartın kendisine tıklanınca
    ): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>(){

    class PlaceViewHolder(val binding: RvItemHomeBinding): RecyclerView.ViewHolder(binding.root)


    // Kart tasarımı XML'e yüklenir
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
       val binding = RvItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceViewHolder(binding)
    }

    // tıklama işlemleri ve verilerin aktarıldığı yer
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place= placeList[position]// Sıradaki mekan verisi

        // verileri arayüz elemanlarına yazdır
        holder.binding.imgPlace.setImageResource(place.imageRes)
        holder.binding.tvName.text = place.name
        holder.binding.tvDistrict.text = place.district

        // Mekan Favoriyse dolu kalp
        if(place.isFavorite){
            holder.binding.btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        }else {
            holder.binding.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
        }

        // Kartın kendisine tıkladığında
        holder.itemView.setOnClickListener {
            onItemClick(place)
        }

        // Rotaya Ekle Butonuna tıklandığında
        holder.binding.btnAdd.setOnClickListener {
            onAddClick(place)
        }

        // favori butonuna tıklandığında
        holder.binding.btnFavorite.setOnClickListener {
            onFavoriteClick(place)
        }
    }

    //Listede eleman sayısı
    override fun getItemCount(): Int {
        return placeList.size
    }

}