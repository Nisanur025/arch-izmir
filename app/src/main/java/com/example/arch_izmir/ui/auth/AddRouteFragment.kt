package com.example.arch_izmir.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.data.local.AppDatabase
import com.example.arch_izmir.data.model.RouteStop
import com.example.arch_izmir.databinding.FragmentAddRouteBinding
import com.example.arch_izmir.ui.adapter.RouteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class AddRouteFragment : Fragment() {

    private var _binding : FragmentAddRouteBinding?= null
    private val binding get()  = _binding!!
    private lateinit var routeAdapter: RouteAdapter
    private lateinit var itemTouchHelper: ItemTouchHelper
    private var currentStops: List<RouteStop> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddRouteBinding.inflate(inflater,container,false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        routeAdapter = RouteAdapter(
            emptyList(),
            onDeleteClick = {stopToDelete ->
                // Tek tek silme butonuna basıldığında çalışacak blok
                val appContext = requireContext().applicationContext
                lifecycleScope.launch(Dispatchers.IO) {
                    val db = AppDatabase.getInstance(appContext)
                    db.routeDao().deleteStopById(stopToDelete.id) // ID'ye göre sil

                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "${stopToDelete.title} rotadan çıkarıldı", Toast.LENGTH_SHORT).show()
                    }
                }

            },
            onStartDrag = { viewHolder ->
                itemTouchHelper.startDrag(viewHolder)
            }
        )


        binding.rvRouteItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRouteItems.adapter = routeAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val db = AppDatabase.getInstance(requireContext())
                db.routeDao().getMyRouteStops().collectLatest { stopList ->
                    currentStops = stopList
                    if (stopList.isEmpty()) {
                        binding.layoutEmptyRoute.visibility = View.VISIBLE
                        binding.layoutFilledRoute.visibility = View.GONE
                    } else {
                        binding.layoutEmptyRoute.visibility = View.GONE
                        binding.layoutFilledRoute.visibility = View.VISIBLE
                        binding.tvRouteSummaryInfo.text = "${stopList.size} Durak seçildi"
                        routeAdapter.updateList(stopList)
                    }
                }
            }
        }


        binding.btnClearRoute.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getInstance(requireContext())
                db.routeDao().deleteAllStops() // Tablodaki tüm durakları sil

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Tüm rota temizlendi", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setupDragAndDrop()

    }
    // Basılı tutunca sürükleme

    private fun setupDragAndDrop() {

        val callback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            0
        ) {

            // Kart başka bir kartın üzerine taşındığında çalışır.
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {

                val fromPosition = viewHolder.bindingAdapterPosition
                val toPosition = target.bindingAdapterPosition

                if (fromPosition == RecyclerView.NO_POSITION ||
                    toPosition == RecyclerView.NO_POSITION
                ) {
                    return false
                }

                routeAdapter.moveItem(fromPosition, toPosition)

                return true
            }

            // Sağa veya sola kaydırarak silme yapmayacağız.
            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                direction: Int
            ) {
                // Kullanılmıyor.
            }

            // Kartın herhangi bir yerine basılı tutarak sürükleme kapalı.
            override fun isLongPressDragEnabled(): Boolean {
                return false
            }

            // Sürükleme tamamlandığında çalışır.
            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)

                saveNewOrder()
            }
        }

        itemTouchHelper = ItemTouchHelper(callback)

        itemTouchHelper.attachToRecyclerView(
            binding.rvRouteItems
        )
    }
    private fun saveNewOrder() {

        val appContext = requireContext().applicationContext

        // Adapter'daki güncel sırayı al.
        val newOrder = routeAdapter.getCurrentStops()

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {

            val db = AppDatabase.getInstance(appContext)

            // Yeni sırayı Room Database'e kaydet.
            db.routeDao().saveOrder(newOrder)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}