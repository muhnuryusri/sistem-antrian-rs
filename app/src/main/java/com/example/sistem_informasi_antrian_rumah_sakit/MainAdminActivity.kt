package com.example.sistem_informasi_antrian_rumah_sakit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sistem_informasi_antrian_rumah_sakit.adapter.QueueAdapter
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityMainAdminBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Queue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database : DatabaseReference
    private lateinit var adapter: QueueAdapter
    private var queueList = ArrayList<Queue>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("Queue")

        adapter = QueueAdapter(this, queueList)
        adapter.setData(queueList)
        adapter.notifyDataSetChanged()
        binding.rvQueue.layoutManager = LinearLayoutManager(this)
        binding.rvQueue.setHasFixedSize(true)

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    binding.tvBelumAdaAntrian.visibility = View.GONE
                    for (data in snapshot.children) {
                        val queue = data.getValue(Queue::class.java)
                        if (queue != null) {
                            queueList.add(queue)
                        }
                    }
                    binding.rvQueue.adapter = QueueAdapter(this@MainAdminActivity, queueList)
                } else {
                    binding.tvBelumAdaAntrian.visibility = View.VISIBLE
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.logout -> {
                auth.signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                this.finish()
                Toast.makeText(this,"Logout berhasil", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}