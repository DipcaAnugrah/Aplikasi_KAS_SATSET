package com.satset.kassatset.ManajemenWarga

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satset.kassatset.databinding.ActivityInputPengumumanBinding
import java.util.Calendar

class TambahPengumuman : AppCompatActivity() {

    private lateinit var binding: ActivityInputPengumumanBinding
    private lateinit var databaseLaporan: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var urlFoto: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database and Storage
        databaseLaporan = FirebaseDatabase.getInstance().getReference("pengumuman")
        storageReference = FirebaseStorage.getInstance().reference.child("pengumuman_images")

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Pengumuman"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Mengirim Pengumuman...")
            setCancelable(false)
        }

        // Set up DatePickerDialog for tanggalPengumuman
        binding.tanggalPengumuman.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up image picker for imagePengumuman
        binding.imagePengumuman.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        // Set up form submission
        binding.buttonSelesai.setOnClickListener {
            if (urlFoto != null) {
                uploadImageToStorage(urlFoto!!)
            } else {
                Snackbar.make(binding.buttonSelesai, "Masukan bukti foto", Snackbar.LENGTH_LONG).show()
            }
        }

        // Set up cancel button
        binding.buttonBatal.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Image Uri will not be null for RESULT_OK
            urlFoto = data?.data
            binding.imagePengumuman.setImageURI(urlFoto)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            // Handle error
            Snackbar.make(binding.imagePengumuman, ImagePicker.getError(data), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(binding.imagePengumuman, "Task Cancelled", Snackbar.LENGTH_LONG).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                binding.tanggalPengumuman.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun uploadImageToStorage(fileUri: Uri) {
        progressDialog.show()
        val fileName = "${System.currentTimeMillis()}.jpg"
        val fileReference = storageReference.child(fileName)

        fileReference.putFile(fileUri)
            .addOnSuccessListener {
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    sendPengumuman(downloadUrl)
                }
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Snackbar.make(binding.buttonSelesai, "Gagal mengunggah gambar", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun sendPengumuman(fotoUrl: String) {
        val judul = binding.judul.text.toString().trim()
        val isiPengumuman = binding.isiPengumuman.text.toString().trim()
        val tanggal = binding.tanggalPengumuman.text.toString().trim()

        if (judul.isEmpty() || isiPengumuman.isEmpty() || tanggal.isEmpty()) {
            progressDialog.dismiss()
            Snackbar.make(binding.buttonSelesai, "Semua field harus diisi", Snackbar.LENGTH_LONG).show()
            return
        }

        val laporanId = databaseLaporan.push().key
        val laporan = Pengumuman(laporanId, judul, isiPengumuman, tanggal, fotoUrl)

        laporanId?.let {
            databaseLaporan.child(it).setValue(laporan)
                .addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        Snackbar.make(binding.buttonSelesai, "Laporan berhasil dikirim", Snackbar.LENGTH_LONG).show()
                        finish()
                    } else {
                        Snackbar.make(binding.buttonSelesai, "Gagal mengirim laporan", Snackbar.LENGTH_LONG).show()
                    }
                }
        }
    }
}
