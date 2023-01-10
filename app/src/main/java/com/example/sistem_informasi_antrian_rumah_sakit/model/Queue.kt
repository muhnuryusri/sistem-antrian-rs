package com.example.sistem_informasi_antrian_rumah_sakit.model

data class Queue(
    var id: String,
    var idUser: String,
    var no: Int? = 0,
    var name: String,
)