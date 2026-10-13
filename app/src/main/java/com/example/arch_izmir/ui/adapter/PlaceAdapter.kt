package com.example.arch_izmir.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.Place

class PlaceAdapter(
    private val placeList: List<Place>, // listede gösterilecek mekanlar
    private val onAddClick: (Place) -> Unit, // Rotaya ekleme
    private val onFavoriteClick:(Place) ->Unit, // Favoriye Ekleme
    private val onItemClick:(Place) -> Unit // Kartın kendisine tıklanınca
    ): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>(){

        // her karttaki gorunumu tutan sınıf
        class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
            // Bileşenlerin idleri
            val ivPlaceImage : ImageView = itemView.findViewById<ImageView>(R.id.imgPlace)
            val tvPlaceTitle : TextView = itemView.findViewById<TextView>(R.id.tvName)
            val tvPlaceSubtitle: TextView = itemView.findViewById<TextView>(R.id.tvDistrict)
            val btnFavorite : ImageButton = itemView.findViewById<ImageButton>(R.id.btnFavorite)
            val btnAddStamp : ImageButton = itemView.findViewById<ImageButton>(R.id.btnAdd)
        }

    // Kart tasarımı XML'e yüklenir
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_home,parent,false)
        return PlaceViewHolder(view)
    }

    // tıklama işlemleri ve verilerin aktarıldığı yer
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place= placeList[position]// Sıradaki mekan verisi

        // verileri arayüz elemanlarına yazdır
        holder.ivPlaceImage.setImageResource(place.imageRes)
        holder.tvPlaceTitle.text = place.name
        holder.tvPlaceSubtitle.text = place.district

        // Mekan Favoriyse dolu kalp
        if(place.isFavorite){
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_filled)
        }else {
            holder.btnFavorite.setImageResource(R.drawable.ic_heart_empty)
        }

        // Kartın kendisine tıkladığında
        holder.itemView.setOnClickListener {
            //TODO : fragment geçişi eklenecek
            //onItemClick(place)
        }

        // Rotaya Ekle Butonuna tıklandığında
        holder.btnAddStamp.setOnClickListener {
            //TODO: Rotaya eklenecek
            //onAddClick(place)
        }

        // favori butonuna tıklandığında
        holder.btnFavorite.setOnClickListener {
            // TODO : Favori sayfasına eklenecek
            //onFavoriteClick(place)
        }



    }

    //Listede toplam kaç eleman olduğu
    override fun getItemCount(): Int {
        return placeList.size
    }

}