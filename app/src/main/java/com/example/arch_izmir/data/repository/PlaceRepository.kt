package com.example.arch_izmir.data.repository

import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.Place

object PlaceRepository {

    val places = listOf(
        Place(1,
            "Efes Antik Kenti",
            "Selçuk",
            R.drawable.img_efes_antik_kenti,
            isFavorite = false,
            isAddedToRoad = false,
            description = "Efes, İyonyalılar tarafından kurulan ve antik dünyanın önemli liman kentlerinden biri olan şehirdir. Yedi Harika'dan biri sayılan Artemis Tapınağı Efes'te yer alıyordu. Roma döneminde Asya eyaletinin önemli merkezi oldu; Celsus Kütüphanesi M.S. 2. yüzyılda yapıldı. Liman zamanla alüvyonla dolunca kent önemini yitirdi. Efes 2015'te UNESCO Dünya Mirası Listesi\'ne alındı.",
            audioGuideSubtitle = "Gezerken neye baktığını anla: 11 durak, sesli anlatım, \"Ne görüyorum?\" ipuçları ",
            distanceKm = 9.0
        ),


        Place(2,
            "Saat Kulesi",
            "Konak",
            R.drawable.img_konak,
            isFavorite = false,
            isAddedToRoad = false,
            description = "Saat Kulesi, 1901'de II. Abdülhamid'in tahta çıkışının 25. yılı anısına Konak Meydanı'na yaptırıldı. Mimarı, İzmir'de yaşayan Levanten Fransız mimar Raymond Charles Péré'dir. Yaklaşık 25 metre yüksekliğindeki kulenin dört yanında Osmanlı üslubunda çeşmeler bulunur. Saat mekanizmasının dönemin Alman İmparatoru II. Wilhelm tarafından hediye edildiği aktarılır. Kule o günden bugüne İzmir'in simgesi sayılır.",
            audioGuideSubtitle = null,
            distanceKm = 9.0
        ),

        Place(3,
            "Meryem Ana Kilisesi",
            "Selçuk",
            R.drawable.img_meryem_ana,
            isFavorite = false,
            isAddedToRoad = false,
            description = "Meryem Ana Evi, İzmir Selçuk'taki Bülbül Dağı üzerinde yer alan kutsal bir mabettir. Hristiyan inancına göre Hz. Meryem, Havari Yuhanna ile Efes\'e gelerek son yıllarını bu evde geçirmiştir. Yapı, 1891 yılında Alman rahibe Anna Katharina Emmerich'in rüyaları rehberliğinde keşfedilmiştir. Vatikan tarafından hac yeri kabul edilen mekânı din adamları ve papalar ziyaret etmekte, bahçesindeki şifalı çeşmeler ile dilek duvarı her yıl binlerce ziyaretçiyi ağırlamaktadır.",
            audioGuideSubtitle = "Gezerken neye baktığını anla: 5 durak, sesli anlatım, \"Ne görüyorum?\" ipuçları ",
            distanceKm = 9.0
        ),


        Place(
            id = 4,
            name = "İzmir Agora Ören Yeri",
            district = "Konak",
            imageRes = R.drawable.img_agora,
            isFavorite = false,
            isAddedToRoad = false,
            description = "İzmir Agora Ören Yeri, İonia bölgesinin en önemli kentlerinden biri olan Smyrna'nın (İzmir) MS 2. yüzyıldan kalma kalıntılarının bulunduğu antik bir pazaryeridir. İpek Yolu'nun bitiş noktasında yer alan bu büyük yapı kompleksi; bazilikası, grafitileri, su kemerleri ve kemerli galerileriyle Roma döneminin günlük ticaret ve sosyal yaşamına ışık tutar.",
            audioGuideSubtitle = "Gezerken neye baktığını anla: 4 durak, sesli anlatım",
            distanceKm = 9.0
        )
    )

    fun getById(id:Int): Place? {
        return places.find{it.id == id}
    }

    // Açık olan mekan hariç diğerleri
    fun getNearby(excludeId: Int): List<Place> {
        return places.filter { it.id != excludeId }
    }

}