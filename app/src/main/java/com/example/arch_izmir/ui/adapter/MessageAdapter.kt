package com.example.arch_izmir.ui.adapter

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.arch_izmir.R
import com.example.arch_izmir.data.model.MessageModel
import com.example.arch_izmir.databinding.ItemMessageBinding
import androidx.core.graphics.toColorInt

class MessageAdapter(private val messageList: MutableList<MessageModel>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    // UI bileşenlerini saklar
    class ViewHolder(val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)

    //yeni view üretir
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    // var olan satıra veriyi yazdırır
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messageList[position]

        holder.binding.tvMessage.text = message.messageText

        if(message.isUser){
            // Sağda turuncu baloncuk
            holder.binding.root.gravity = Gravity.END
            holder.binding.tvMessage.setBackgroundResource(R.drawable.bg_user_message)
            holder.binding.tvMessage.setTextColor("#F2E6C9".toColorInt())
        }else{
            // Solda mor baloncuk
            holder.binding.root.gravity = Gravity.START
            holder.binding.tvMessage.setBackgroundResource(R.drawable.bg_bot_message)
            holder.binding.tvMessage.setTextColor("#F2E6C9".toColorInt())
        }

    }

    // Listenin toplam eleman sayısını döndürür
    override fun getItemCount(): Int {
        return messageList.size
    }

    // Mesaj ekle
    fun addMessage(message: MessageModel){
        messageList.add(message)
        notifyItemInserted(messageList.size-1)
    }

    // Mesaj sil
    fun clearMessage(){
        messageList.clear()
        notifyDataSetChanged()
    }

}