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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arch_izmir.data.local.AppDatabase
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
        routeAdapter = RouteAdapter(emptyList()) { stopToDelete ->
            // Tek tek silme butonuna basıldığında çalışacak blok
            lifecycleScope.launch(Dispatchers.IO) {
                val db = AppDatabase.getInstance(requireContext())
                db.routeDao().deleteStopById(stopToDelete.id) // ID'ye göre sil

                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "${stopToDelete.title} rotadan çıkarıldı", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.rvRouteItems.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRouteItems.adapter = routeAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val db = AppDatabase.getInstance(requireContext())
                db.routeDao().getMyRouteStops().collectLatest { stopList ->
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


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}