package com.example.arch_izmir.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.arch_izmir.data.repository.PlaceRepository
import com.example.arch_izmir.databinding.FragmentFavoriteBinding
import com.example.arch_izmir.ui.adapter.PlaceAdapter


class FavoriteFragment : Fragment() {

    private var _binding : FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter : PlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favoriteAdapter = PlaceAdapter(
            placeList = PlaceRepository.favoritePlaces,
            onAddClick = {place ->
                // TODO: rotaya eklenecek
            },
            onFavoriteClick ={ place ->
                PlaceRepository.toggleFavorite(place)
                favoriteAdapter.notifyDataSetChanged()
            },
            onItemClick = { place ->
                val action = FavoriteFragmentDirections.actionFavoriteFragmentToPlaceDetailFragment(place.id)
                findNavController().navigate(action)
            }
        )
        binding.rvPlacesFavorite.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            }
            adapter = favoriteAdapter
            addItemDecoration(HomeFragment.SpacingDecoration((6 * resources.displayMetrics.density).toInt()))
        }

    }

    override fun onResume() {
        super.onResume()
        favoriteAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}