package com.example.sistem_informasi_antrian_rumah_sakit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}