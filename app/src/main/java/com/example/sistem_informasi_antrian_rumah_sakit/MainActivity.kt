package com.example.sistem_informasi_antrian_rumah_sakit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityMainBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Queue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseQueue: DatabaseReference
    private lateinit var databaseAuth: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        databaseQueue = FirebaseDatabase.getInstance().reference.child("Queue")
        databaseAuth =
            auth.currentUser?.let { FirebaseDatabase.getInstance().reference.child("Users").child(it.uid).child("name") }!!

        var number = 1

        databaseQueue.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val antrianSekarang = databaseQueue.orderByKey().limitToFirst(1)
                antrianSekarang.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (data in snapshot.children) {
                            val idUser = data.child("idUser").value
                            val antrian = data.child("no").value

                            binding.tvNoAntrianSekarang.text = antrian.toString()
                            if (idUser == auth.currentUser!!.uid) {
                                binding.tvNoAntrianDipanggil.visibility = View.VISIBLE
                            } else {
                                binding.tvNoAntrianDipanggil.visibility = View.GONE
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.btnDaftarAntrian.setOnClickListener {
            databaseAuth.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val queue = snapshot.value.toString()
                    addQueue(number++, queue)
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this,"Logout berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun addQueue(noAntrian: Int, namaAntrian: String) {
        auth = FirebaseAuth.getInstance()
        databaseQueue = FirebaseDatabase.getInstance().reference.child("Queue")

        val id = databaseQueue.push().key

        val queue = id?.let { auth.currentUser?.let { it1 -> Queue(it, it1.uid, noAntrian, namaAntrian) } }

        binding.tvNoAntrian.text = noAntrian.toString()
//        binding.btnDaftarAntrian.isEnabled = false

        if (id != null) {
            databaseQueue.child(id).setValue(queue).addOnSuccessListener {
                Toast.makeText(this@MainActivity, "Anda ditambahkan ke antrian", Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity,"Antrian gagal ditambahkan", Toast.LENGTH_SHORT).show()
                }
        }
    }
}