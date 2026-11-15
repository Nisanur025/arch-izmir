package com.example.arch_izmir.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.Badge
import com.example.arch_izmir.data.model.BadgeState
import com.example.arch_izmir.data.model.ProgressRow
import com.example.arch_izmir.data.repository.PlaceRepository
import com.example.arch_izmir.databinding.FragmentPassportBinding
import com.example.arch_izmir.ui.adapter.BadgeAdapter
import com.example.arch_izmir.ui.adapter.ProgressRowAdapter
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
        setupStats(visitedPlaces.size)
        setupBadges()
        setupTabs()
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
    private fun setupTabs() = with(binding) {
        tabDiscoveries.setOnClickListener { selectTab(it) }
        tabBadges.setOnClickListener { selectTab(it) }
        tabStats.setOnClickListener { selectTab(it) }
        selectTab(tabDiscoveries)
    }
    private fun setupStats(visitedCount:Int) =with(binding) {
        // Özet kartlar
        statPearlValue.text = "30"
        statVisitedValue.text = "$visitedCount / $totalHeritageCount"
        statStreakValue.text = "2 gün"

        // Kişilik kartı
        personalityTitle.text = "Antik Kent Avcısı"
        personalityDesc.text = "Gezdiğin yerlerin %50'si antik dönemden."

        // Kategoriler (en çok olan en üstte)
        val categories = listOf(
            ProgressRow("Antik kent ve kalıntı", 2, 4, "2 yer"),
            ProgressRow("Tarihi simge", 1, 4, "1 yer"),
            ProgressRow("Dini mekanlar", 1, 4, "1 yer")
        )
        categoryRecycler.layoutManager = LinearLayoutManager(requireContext())
        categoryRecycler.adapter = ProgressRowAdapter(categories)

        // Bölgeler (ilerlemeye göre sıralı)
        val regions = listOf(
            Triple("Selçuk", 2, 5),
            Triple("Konak", 2, 7),
            Triple("Karşıyaka", 0, 4),
            Triple("Bergama", 0, 3),
            Triple("Urla", 0, 3),
            Triple("Çeşme", 0, 3),
            Triple("Foça", 0, 2)
        ).sortedByDescending { it.second.toFloat() / it.third }
            .map { (name, visited, total) ->
                ProgressRow(name, visited, total, "$visited / $total")
            }
        regionRecycler.layoutManager = LinearLayoutManager(requireContext())
        regionRecycler.adapter = ProgressRowAdapter(regions)
    }
    private fun selectTab(selected: View) =with(binding) {
        placesSection.isVisible = selected == tabDiscoveries
        statsSection.isVisible = selected == tabStats
        badgesSection.isVisible = selected == tabBadges

        listOf(tabDiscoveries, tabBadges, tabStats).forEach { tab ->
            val on = tab == selected
            tab.setBackgroundResource(if (on) R.drawable.bg_tab_selected else R.drawable.bg_tab_unselected)
            tab.setTextColor(
                ContextCompat.getColor(requireContext(), if (on) R.color.passport_text else R.color.passport_text_soft)
            )
        }
    }

    private fun setupBadges() = with(binding) {
        val badges = listOf(
            Badge("İlk Adım", "1 yer keşfet", BadgeState.EARNED, dateText = "12 Eki 2026"),
            Badge("Antik Kaşif", "2 antik yer gez", BadgeState.EARNED, dateText = "12 Eki 2026"),
            Badge("Gönül Gezgini", "Bir inanç mekânı gez", BadgeState.EARNED, dateText = "3 Kas 2026"),
            Badge("Selçuk Uzmanı", "Selçuk'ta 5 yer gez", BadgeState.IN_PROGRESS, value = 2, max = 5),
            Badge("Konak Gezgini", "Konak'ta 7 yer gez", BadgeState.IN_PROGRESS, value = 2, max = 7),
            Badge("10 Yer Keşfet", "Toplam 10 yer gez", BadgeState.IN_PROGRESS, value = 4, max = 10),
            Badge("Karşıyaka Kaşifi", "Karşıyaka'da 4 yer gez", BadgeState.LOCKED, value = 0, max = 4),
            Badge("İzmir'in İncisi", "Tüm 27 yeri keşfet", BadgeState.LOCKED, value = 4, max = 27)
        )

        val earned = badges.count { it.state == BadgeState.EARNED }
        badgeSummary.text = "Rozet koleksiyonu: $earned / ${badges.size}"

        badgeRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        badgeRecycler.adapter = BadgeAdapter(badges)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}