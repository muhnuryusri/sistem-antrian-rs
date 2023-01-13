package com.example.sistem_informasi_antrian_rumah_sakit

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityMainBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Queue
import com.google.android.gms.tasks.Task
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

        val number = 1

        databaseQueue.addValueEventListener(object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val antrianSekarang = databaseQueue.orderByKey().limitToFirst(1)
                    antrianSekarang.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data in snapshot.children) {
                                val idUser = data?.child("idUser")?.value
                                val antrian = data?.child("no")?.getValue(Int::class.java)

                                binding.tvNoAntrianSekarang.text = antrian.toString()

                                if (idUser.toString() == auth.currentUser?.uid.toString()) {
                                    binding.tvNoAntrian.text = antrian.toString()
                                    binding.tvNoAntrianDipanggil.visibility = View.VISIBLE
                                    binding.btnDaftarAntrian.isEnabled = false
                                } else {
                                    binding.tvNoAntrianDipanggil.visibility = View.GONE
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })

                    val antrianTerbaru = databaseQueue.orderByKey().limitToLast(1)
                    antrianTerbaru.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (data in snapshot.children) {
                                val antrian = data?.child("no")?.getValue(Int::class.java)
                                binding.btnDaftarAntrian.setOnClickListener {
                                    databaseAuth.addValueEventListener(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val queue = snapshot.value.toString()
                                            if (antrian != null) {
                                                addQueue(antrian+1, queue)
                                            }
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                        }
                                    })
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }

                    })
                } else {
                    binding.apply {
                        tvNoAntrianSekarang.apply {
                            text = "Belum ada antrian"
                            textSize = 20F
                            setTextColor(Color.RED)
                        }
                        btnDaftarAntrian.setOnClickListener {
                            databaseAuth.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val queue = snapshot.value.toString()
                                    addQueue(number, queue)
                                }

                                override fun onCancelled(error: DatabaseError) {
                                }
                            })
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        binding.btnLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            this.finish()
            Toast.makeText(this,"Logout berhasil", Toast.LENGTH_SHORT).show()
        }
    }

    internal fun addQueue(noAntrian: Int, namaAntrian: String) {
        auth = FirebaseAuth.getInstance()
        databaseQueue = FirebaseDatabase.getInstance().reference.child("Queue")

        val id = databaseQueue.push().key

        val queue = id?.let { auth.currentUser?.let { it1 -> Queue(it, it1.uid, noAntrian, namaAntrian) } }

        binding.tvNoAntrian.text = noAntrian.toString()
        binding.btnDaftarAntrian.isEnabled = false

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