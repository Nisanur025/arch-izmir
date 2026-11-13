package com.example.arch_izmir.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arch_izmir.R
import com.example.arch_izmir.data.repository.PlaceRepository
import com.example.arch_izmir.databinding.FragmentPassportBinding
import com.example.arch_izmir.ui.adapter.VisitedPlaceAdapter

class PassportFragment : Fragment() {

    private var _binding: FragmentPassportBinding? = null
    private val binding get() = _binding!!

    // Toplam keşfedilecek miras sayısı (şimdilik sabit)
    private val totalHeritageCount = 27

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPassportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val visitedPlaces = PlaceRepository.places.filter { it.isVisited }

        setupProgress(visitedPlaces.size)
        setupPlacesRecycler(visitedPlaces)
    }

    // İlerleme kartı: yüzde, sayaç ve dairesel gösterge
    private fun setupProgress(discovered: Int) {
        val percent = if (totalHeritageCount == 0) 0 else discovered * 100 / totalHeritageCount

        binding.percentText.text = getString(R.string.discovery_percent_format, percent)
        binding.discoveryCountText.text =
            getString(R.string.discovery_count_format, discovered, totalHeritageCount)
        binding.discoveryProgress.progress = percent
    }

    // Gezdiğin Yerler yatay listesi
    private fun setupPlacesRecycler(visitedPlaces: List<com.example.arch_izmir.data.model.Place>) {
        binding.placesRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        binding.placesRecycler.adapter = VisitedPlaceAdapter(visitedPlaces) { place ->
            // TODO: Detay sayfasına git
            // val bundle = Bundle().apply { putInt("placeId", place.id) }
            // findNavController().navigate(R.id.action_passportFragment_to_detailFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}