package com.example.sistem_informasi_antrian_rumah_sakit.model

import com.google.android.gms.tasks.Task

data class Queue(
    var id: String? = null,
    var idUser: String? = null,
    var no: Int? = null,
    var name: String? = null,
)