package com.example.sistem_informasi_antrian_rumah_sakit.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ItemQueueBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Queue

class QueueAdapter (private val queueList: ArrayList<Queue>) : RecyclerView.Adapter<QueueAdapter.ItemViewHolder>() {
    fun setData(data: List<Queue>) {
        queueList.clear()
        queueList.addAll(data)
        this.notifyItemChanged(0)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemQueueBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position == 0) {
            holder.binding.apply {
                cardQueue.strokeColor = Color.GREEN
                tvAntrianGuide.text = "Antrian Dipanggil"
                tvAntrianGuide.setTextColor(Color.GREEN)
            }
        } else {
            holder.bind(queueList[position])
        }
    }

    override fun getItemCount(): Int = queueList.size

    inner class ItemViewHolder(var binding: ItemQueueBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Queue) {
            binding.apply {
                tvNamaPasien.text = data.name
                tvNoAntrian.text = data.no.toString()
            }
        }
    }
}