package com.example.sistem_informasi_antrian_rumah_sakit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityLoginAdminBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.Admin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LoginAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.btnLogin.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() && password.isEmpty()) {
                Toast.makeText(this@LoginAdminActivity, "Masukkan username dan password anda", Toast.LENGTH_SHORT).show()
            } else {
                database.child("Admin").addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        var isLoginMatch : Boolean? = false
                        var dataEmail : String? = null
                        var dataPassword : String?

                        for (adminSnapshot in dataSnapshot.children) {
                            val admin = adminSnapshot.getValue(Admin::class.java)

                            dataEmail = admin?.email
                            dataPassword = admin?.password

                            if (dataEmail == email && dataPassword == password) {
                                isLoginMatch = true
                                break
                            }
                        }

                        if (isLoginMatch == true) {
                            val intent = Intent(this@LoginAdminActivity, MainAdminActivity::class.java)
                            intent.putExtra("logged_in_username", dataEmail)
                            startActivity(intent)
                            Toast.makeText(this@LoginAdminActivity,"Login berhasil", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginAdminActivity,"Username atau password anda salah",
                                Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Getting user failed
                        Toast.makeText(this@LoginAdminActivity,"User tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.navLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}