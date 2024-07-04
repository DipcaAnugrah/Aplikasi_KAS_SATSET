package com.satset.kassatset.ManajemenWarga

data class Warga(
    var id: String? = null,
    var namaLengkap: String? = null,
    var jenisKelamin: String? = null,
    var alamat: String? = null,
    var nomorTelepon: String? = null,
    var email: String? = null,
    var statusPernikahan: String? = null,
    var statusKeaktifan: String? = null,
    var pekerjaan: String? = null,
    var namaPasangan: String? = null,
    var jumlahAnak: Int? = null,
    var jumlahIuran: Int? = null,
    var jenisIuran: String? = null,
    var tanggalPembayaran: String? = null,
    var urlFoto: String? = null
)
