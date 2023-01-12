package com.example.sistem_informasi_antrian_rumah_sakit.adapter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ItemQueueBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Queue
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class QueueAdapter (val context: Context?, private val queueList: ArrayList<Queue>) : RecyclerView.Adapter<QueueAdapter.ItemViewHolder>() {
    private lateinit var database: DatabaseReference

    fun setData(data: List<Queue>) {
        queueList.clear()
        queueList.addAll(data)
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemQueueBinding.inflate(LayoutInflater.from(parent.context))
        return ItemViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position == 0) {
            holder.bind(queueList[0])
            holder.binding.apply {
                val queue = queueList[position]
                cardQueue.setCardBackgroundColor(Color.GREEN)
                tvAntrianGuide.text = "Antrian Dipanggil"
                tvNamaPasien.setTextColor(Color.WHITE)
                tvNoAntrian.setTextColor(Color.WHITE)
                tvAntrianGuide.setTextColor(Color.WHITE)
                cardQueue.setOnClickListener {
                    showDeleteDialog(queue)
                }
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

    fun showDeleteDialog(queue: Queue) {
        val builder = AlertDialog.Builder(context)

        builder.setTitle("Hapus Antrian")
        builder.setMessage("Anda ingin menghapus antrian ini?")

        database = FirebaseDatabase.getInstance().reference.child("Queue")

        builder.setPositiveButton("Ya") { _, _ ->
            queue.id?.let { database.child(it).removeValue() }
            setData(queueList)
        }

        builder.setNegativeButton("Tidak") { _, _ ->
        }

        val alert = builder.create()
        alert.show()
    }
}