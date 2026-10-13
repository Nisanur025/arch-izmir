    package com.example.arch_izmir.ui.auth

    import android.graphics.Rect
    import android.os.Bundle
    import androidx.fragment.app.Fragment
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.recyclerview.widget.RecyclerView
    import androidx.recyclerview.widget.StaggeredGridLayoutManager
    import com.example.arch_izmir.R
    import com.example.arch_izmir.data.model.Place
    import com.example.arch_izmir.databinding.FragmentHomeBinding
    import com.example.arch_izmir.ui.adapter.PlaceAdapter


    class HomeFragment : Fragment() {
        private lateinit var binding: FragmentHomeBinding

        // Adapter
        private var placeAdapter = PlaceAdapter(
            placeList = samplePlaces(),
            onAddClick = {place -> /*Rotaya ekle*/ },
            onFavoriteClick = {place -> /*Favorilere ekle*/},
            onItemClick={place -> /*Detay sayfasına git*/}
        )

        private fun samplePlaces(): List<Place>{

            return listOf(
                Place(1,"Efes Antik Kenti","Selçuk",R.drawable.img_efes_antik_kenti),
                Place(2,"Saat Kulesi","Konak",R.drawable.img_konak),
                Place(3,"Meryem Ana Kilisesi","Selçuk",R.drawable.img_meryem_ana),
                Place(4,"Agora Ören Yeri","Konak-Namazgah",R.drawable.img_agora)

            )
        }
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