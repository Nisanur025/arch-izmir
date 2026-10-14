package com.example.arch_izmir.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.arch_izmir.R
import com.example.arch_izmir.data.repository.PlaceRepository
import com.example.arch_izmir.databinding.FragmentPlaceDetailBinding


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


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}