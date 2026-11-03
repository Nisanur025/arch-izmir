    package com.example.arch_izmir.ui.auth

    import android.graphics.Rect
    import android.os.Bundle
    import android.text.Editable
    import android.text.TextWatcher
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import android.widget.Toast
    import androidx.lifecycle.lifecycleScope
    import androidx.navigation.fragment.findNavController
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.StaggeredGridLayoutManager
    import com.example.arch_izmir.data.local.AppDatabase
    import com.example.arch_izmir.data.model.RouteStop
    import com.example.arch_izmir.data.repository.PlaceRepository
    import com.example.arch_izmir.databinding.FragmentHomeBinding
    import com.example.arch_izmir.ui.adapter.PlaceAdapter
    import kotlinx.coroutines.Dispatchers
    import kotlinx.coroutines.launch
    import kotlinx.coroutines.withContext


    class HomeFragment : Fragment() {
        private var _binding: FragmentHomeBinding? = null
        private val binding get() = _binding!!
        private lateinit var placeAdapter: PlaceAdapter

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
           _binding = FragmentHomeBinding.inflate(inflater, container, false)
            return binding.root
        }

        class SpacingDecoration(private val spacePx: Int) : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                outRect.set(spacePx, spacePx, spacePx, spacePx)
            }
        }



        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            PlaceRepository.init(requireContext())

            // Adapter
            placeAdapter = PlaceAdapter(
                placeList = PlaceRepository.places,
                onAddClick = { place -> // Rota Ekleme
                    val placeName = place.name
                    val placeLocation = place.district
                    val placeLatitude = place.latitude
                    val placeLongitude = place.longitude

                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                        val appContext = requireContext().applicationContext
                        val routeDao = AppDatabase.getInstance(appContext).routeDao()
                        val currentCount = routeDao.getMaxOrderIndex() + 1

                        routeDao.insertStop(
                            RouteStop(
                                title = placeName,
                                location = placeLocation,
                                visitDuration = "~ 60 dk ziyaret",
                                latitude = placeLatitude,
                                longitude = placeLongitude,
                                orderIndex = currentCount
                            )
                        )

                        withContext(Dispatchers.Main){
                            Toast.makeText(appContext,"$placeName rotaya eklendi!", Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onFavoriteClick = { place ->
                    PlaceRepository.toggleFavorite(place)
                    placeAdapter.notifyDataSetChanged() // kalbi günceller
                },
                onItemClick = { place ->
                    val action = HomeFragmentDirections.actionHomeFragmentToPlaceDetailFragment(place.id)
                    findNavController().navigate(action)
                }
            )

            // rv adapter bağla
            binding.rvPlaces.apply {
                layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL).apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
                adapter = placeAdapter
                addItemDecoration(SpacingDecoration((6 * resources.displayMetrics.density).toInt()))
            }

            binding.etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    placeAdapter.filter(s.toString())
                }
                override fun afterTextChanged(s: Editable?) {}
            })
        }
        // Favoriler sayfasında kalp kaldırılıp geri dönünce Home güncellensin
        override fun onResume() {
            super.onResume()
            placeAdapter.notifyDataSetChanged()
        }
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    }