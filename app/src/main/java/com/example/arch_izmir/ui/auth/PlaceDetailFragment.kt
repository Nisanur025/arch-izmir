package com.example.arch_izmir.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arch_izmir.data.local.AppDatabase
import com.example.arch_izmir.data.model.RouteStop
import com.example.arch_izmir.data.repository.PlaceRepository
import com.example.arch_izmir.databinding.FragmentPlaceDetailBinding
import com.example.arch_izmir.ui.adapter.NearbyPlacesAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class PlaceDetailFragment : Fragment() {

    private var _binding : FragmentPlaceDetailBinding? = null
    private val binding get()=_binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentPlaceDetailBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val placeId = requireArguments().getInt("placeId")
        val place= PlaceRepository.getById(placeId) ?: return

        binding.tvTitle.text = place.name
        binding.tvLocation.text = place.district
        binding.cardHistory.tvCardBody.text = place.description
        binding.tvRehberDesc.text = place.audioGuideSubtitle
        binding.ivCover.setImageResource(place.imageRes)

        // favori kontrolü
        binding.btnFavorite.isSelected = place.isFavorite

        binding.btnFavorite.setOnClickListener {
            PlaceRepository.toggleFavorite(place)
            binding.btnFavorite.isSelected=place.isFavorite
        }

        // Geri Butonu
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        // Yakın Mekan Adapter
        val nearbyAdapter = NearbyPlacesAdapter(
            places = PlaceRepository.getNearby(excludeId = place.id),
            onItemClick = { nearby ->
                val action = PlaceDetailFragmentDirections
                    .actionPlaceDetailFragmentSelf(nearby.id)
                findNavController().navigate(action)
            }
        )
        binding.rvNearby.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNearby.adapter = nearbyAdapter

        // Sesli Rehber olmaması gereken yerlerde görünümü kapa
        if (place.audioGuideSubtitle != null) {
            binding.btnYerindeRehber.visibility = View.VISIBLE
            binding.tvRehberDesc.text = place.audioGuideSubtitle
        } else {
            binding.btnYerindeRehber.visibility = View.GONE
        }


        // Rotaya Ekle butonu
        binding.btnAddRoute.setOnClickListener {
            val placeName = place.name
            val placeLocation = place.district
            val visitDuration = "~ 60 dk ziyaret"
            val placeLatitude = place.latitude
            val placeLongitude = place.longitude


            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getInstance(requireContext())
                val routeDao = db.routeDao()

                // Mevcut durak sayısını al
                val currentCount = routeDao.getRouteCount()

                // Yeni durağı oluştur
                val newStop = RouteStop(
                    title = placeName,
                    location = placeLocation,
                    visitDuration = visitDuration,
                    latitude = placeLatitude,
                    longitude = placeLongitude,
                    orderIndex = currentCount + 1
                )

                // Veritabanına ekle
                routeDao.insertStop(newStop)

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "$placeName rotaya eklendi!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}