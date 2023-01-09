package com.example.sistem_informasi_antrian_rumah_sakit.model

import java.util.LinkedList

data class Queue (
    var antrian: LinkedList<QueueList>,
)

data class QueueList (
    var no: Int = 0
)