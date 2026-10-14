    package com.example.arch_izmir.ui.auth

    import android.graphics.Rect
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.StaggeredGridLayoutManager
    import com.example.arch_izmir.R
    import com.example.arch_izmir.data.model.Place
    import com.example.arch_izmir.data.repository.PlaceRepository
    import com.example.arch_izmir.databinding.FragmentHomeBinding
    import com.example.arch_izmir.ui.adapter.PlaceAdapter


    class HomeFragment : Fragment() {
        private lateinit var binding: FragmentHomeBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
        }

        class SpacingDecoration(private val spacePx: Int) : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(spacePx, spacePx, spacePx, spacePx)
            }
        }

        // Adapter
        private var placeAdapter = PlaceAdapter(
            placeList = PlaceRepository.places,
            onAddClick = {place -> /*Rotaya ekle*/ },
            onFavoriteClick = {place -> /*Favorilere ekle*/},
            onItemClick={place ->
                val action = HomeFragmentDirections.actionHomeFragmentToPlaceDetailFragment(place.id)
                findNavController().navigate(action)
            }
        )

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            // rv adapter bağla
            binding.rvPlaces.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
                adapter = placeAdapter
                addItemDecoration(SpacingDecoration((6 * resources.displayMetrics.density).toInt()))
            }

        }


    }