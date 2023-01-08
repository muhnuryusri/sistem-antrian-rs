package com.example.sistem_informasi_antrian_rumah_sakit

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityRegisterBinding
import com.example.sistem_informasi_antrian_rumah_sakit.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        binding.btnRegister.setOnClickListener {
            val id = database.push().key
            val email = binding.email.text.toString()
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (email.isEmpty() && username.isEmpty() && password.isEmpty()) {
                Toast.makeText(this@RegisterActivity, "Mohon lengkapi data anda", Toast.LENGTH_SHORT).show()
            } else {
                val user = id?.let { it1 -> User(it1, email, username, password) }
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val userAuth = auth.currentUser

                            auth.uid?.let { it1 ->
                                database.child(it1).setValue(user).addOnSuccessListener {
                                    Toast.makeText(this,"Registrasi berhasil",Toast.LENGTH_SHORT).show()
                                    updateUI(userAuth)
                                }.addOnFailureListener{
                                    Toast.makeText(this,"Registrasi gagal",Toast.LENGTH_SHORT).show()
                                }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}