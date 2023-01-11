package com.example.sistem_informasi_antrian_rumah_sakit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistem_informasi_antrian_rumah_sakit.adapter.QueueAdapter
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityMainAdminBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Queue
import com.google.firebase.database.*

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var database : DatabaseReference
    private var queueList = ArrayList<Queue>()
    private var adapter = QueueAdapter(queueList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Queue")

        adapter.setData(queueList)
        binding.rvQueue.layoutManager = LinearLayoutManager(this)
        binding.rvQueue.setHasFixedSize(true)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val queue = data.getValue(Queue::class.java)
                        if (queue != null) {
                            queueList.add(queue)
                        }
                    }
                    binding.rvQueue.adapter = QueueAdapter(queueList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}