package com.example.arch_izmir.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arch_izmir.R
import com.example.arch_izmir.data.repository.PlaceRepository
import com.example.arch_izmir.databinding.FragmentPlaceDetailBinding
import com.example.arch_izmir.ui.adapter.NearbyPlacesAdapter


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
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}