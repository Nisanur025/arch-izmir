package com.example.arch_izmir.ui.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.Place
import com.example.arch_izmir.databinding.RvItemHomeBinding
import java.util.Locale

class PlaceAdapter(
    private val placeList: List<Place>, // listede gösterilecek mekanlar
    private val onAddClick: (Place) -> Unit, // Rotaya ekleme
    private val onFavoriteClick:(Place) ->Unit, // Favoriye Ekleme
    private val onItemClick:(Place) -> Unit // Kartın kendisine tıklanınca
    ): RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>(){

        // Filtelenmiş öğeleri içinde tutan liste
        private var filteredList : List<Place> = placeList
    class PlaceViewHolder(val binding: RvItemHomeBinding): RecyclerView.ViewHolder(binding.root)


    // Kart tasarımı XML'e yüklenir
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
       val binding = RvItemHomeBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaceViewHolder(binding)
    }

    // tıklama işlemleri ve verilerin aktarıldığı yer
    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = filteredList[position]// Sıradaki mekan verisi


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


// Dışarıdan yeni liste
    fun updateList(newList: List<Place>) {
        filteredList = newList
        notifyDataSetChanged()
    }
    // Arama filtreleme fonksiyonu
    fun filter(query: String) {
        filteredList = if (query.isEmpty()) {
            placeList // Arama boşsa tüm listeyi göster
        } else {
            val resultList = ArrayList<Place>()
            for (place in placeList) {
                if (place.name.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault())) ||
                    place.district.lowercase(Locale.getDefault()).contains(query.lowercase(Locale.getDefault()))) {
                    resultList.add(place)
                }
            }
            resultList
        }
        notifyDataSetChanged() // RecyclerView'ı yenile
    }

    //Listede eleman sayısı
    override fun getItemCount(): Int {
        return filteredList.size
    }

}