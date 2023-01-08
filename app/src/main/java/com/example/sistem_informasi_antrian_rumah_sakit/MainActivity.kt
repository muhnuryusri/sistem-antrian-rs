package com.example.sistem_informasi_antrian_rumah_sakit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sistem_informasi_antrian_rumah_sakit.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}