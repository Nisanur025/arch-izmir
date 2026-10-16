package com.example.arch_izmir.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.arch_izmir.data.model.MessageModel
import com.example.arch_izmir.databinding.FragmentGuideBinding
import com.example.arch_izmir.databinding.ItemChipBinding
import com.example.arch_izmir.ui.adapter.MessageAdapter


class GuideFragment : Fragment() {

    private var _binding : FragmentGuideBinding?= null
    private val binding get()  = _binding!!
    private lateinit var chatAdapter : MessageAdapter

    private val welcomeText = "Merhaba, ben İzmir Rehberi:)\nİzmir hakkında ne öğrenmek istersin?"
    // soru cevap
    private val Questions = mapOf(
        "İzmir'e nasıl gelirim?" to
                "Adnan Menderes Havalimanı'na uçakla gelebilir, havalimanından İZBAN ile şehir merkezine geçebilirsin. Otobüs ve tren seçenekleri de var.",
        "Şehir içinde nasıl dolaşırım" to
                "Metro, tramvay, İZBAN, otobüs ve vapur kullanabilirsin. Tek bir ulaşım kartı hepsinde geçerli.",
        "Nerede ne yenir?" to
                "Boyoz ve gevrek ile güne başla, öğlen İzmir köfte ye, Kemeraltı'nda kumru dene.",
        "Önce nereyi gezmeliyim?" to
                "Saat Kulesi ve Kemeraltı şehir içi için, Efes Antik Kenti ve Meryem Ana Evi Selçuk için güzel bir başlangıç."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGuideBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // adapter bağla
        chatAdapter = MessageAdapter(mutableListOf())

        binding.rvMessages.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMessages.adapter = chatAdapter

        // ilk karşılama mesajı
        chatAdapter.addMessage(MessageModel(welcomeText, false))

        // Soru butonlarını oluştur chip
        for (question in Questions.keys) {
            val chip = ItemChipBinding.inflate(layoutInflater, binding.chipContainer, false)
            chip.tvChip.text = question
            chip.tvChip.setOnClickListener { sendMessage(question) }
            binding.chipContainer.addView(chip.root)
        }

        // gönder butonu
        binding.btnSend.setOnClickListener {
            sendMessage(binding.etMessage.text.toString())
        }


        // temizle butonu
        binding.tvClear.setOnClickListener {
            chatAdapter.clearMessage()
            chatAdapter.addMessage(MessageModel(welcomeText,false))
        }

    }

    private fun sendMessage(text:String){
        val question = text.trim()
        if(question.isEmpty()) return

        // kullanıcı mesajı
        chatAdapter.addMessage(MessageModel(question,true))

        // bot cevabı
        val answer = Questions[question] ?: "Şu an sadece sık sorulan sorulara cevap verebiliyorum. Aşağıdaki sorulardan birini deneyebilirsin."

        chatAdapter.addMessage(MessageModel(answer,false))

        binding.etMessage.text?.clear()
        binding.rvMessages.scrollToPosition(chatAdapter.itemCount-1)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}