package com.satset.kassatset.Laporan

import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.github.dhaval2404.imagepicker.ImagePicker
import com.satset.kassatset.R
import com.satset.kassatset.databinding.ActivityReportBinding
import java.util.Calendar

class LaporanActivity : AppCompatActivity() {

    private lateinit var inputNama: EditText
    private lateinit var inputTelepon: EditText
    private lateinit var inputLokasi: EditText
    private lateinit var inputTanggal: EditText
    private lateinit var inputLaporan: EditText
    private lateinit var imageLaporan: ImageView
    private lateinit var fabSend: ExtendedFloatingActionButton
    private lateinit var databaseLaporan: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var fotoUri: Uri? = null
    private lateinit var binding: ActivityReportBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database and Storage
        databaseLaporan = FirebaseDatabase.getInstance().getReference("laporan")
        storageReference = FirebaseStorage.getInstance().reference.child("laporan_images")

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Laporan Warga"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize views
        inputNama = findViewById(R.id.inputNama)
        inputTelepon = findViewById(R.id.inputTelepon)
        inputLokasi = findViewById(R.id.inputLokasi)
        inputTanggal = findViewById(R.id.inputTanggal)
        inputLaporan = findViewById(R.id.inputLaporan)
        imageLaporan = findViewById(R.id.imageLaporan)
        fabSend = findViewById(R.id.fabSend)

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Mengirim laporan...")
        progressDialog.setCancelable(false)

        // Set up DatePickerDialog for inputTanggal
        inputTanggal.setOnClickListener {
            showDatePickerDialog()
        }

        imageLaporan.setOnClickListener {
            // Pilih gambar dari galeri atau kamera
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        fabSend.setOnClickListener {
            if (fotoUri != null) {
                uploadImageToStorage(fotoUri!!)
            } else {
                Snackbar.make(fabSend, "Masukan bukti foto", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            // Image Uri will not be null for RESULT_OK
            val fileUri = data?.data
            fotoUri = fileUri
            imageLaporan.setImageURI(fileUri)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            // Handle error
            Snackbar.make(imageLaporan, ImagePicker.getError(data), Snackbar.LENGTH_LONG).show()
        } else {
            Snackbar.make(imageLaporan, "Task Cancelled", Snackbar.LENGTH_LONG).show()
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
                // Format date as dd/mm/yyyy
                val selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                inputTanggal.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun uploadImageToStorage(fileUri: Uri) {
        progressDialog.show()
        val fileName = System.currentTimeMillis().toString() + ".jpg"
        val fileReference = storageReference.child(fileName)

        fileReference.putFile(fileUri)
            .addOnSuccessListener { taskSnapshot ->
                fileReference.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    sendLaporan(downloadUrl)
                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Snackbar.make(fabSend, "Gagal mengunggah gambar", Snackbar.LENGTH_LONG).show()
            }
    }

    private fun sendLaporan(fotoUrl: String) {
        val nama = inputNama.text.toString().trim()
        val telepon = inputTelepon.text.toString().trim()
        val lokasi = inputLokasi.text.toString().trim()
        val tanggal = inputTanggal.text.toString().trim()
        val isi = inputLaporan.text.toString().trim()

        if (nama.isEmpty() || telepon.isEmpty() || lokasi.isEmpty() || tanggal.isEmpty() || isi.isEmpty()) {
            progressDialog.dismiss()
            // Tampilkan pesan kesalahan atau validasi
            Snackbar.make(fabSend, "Semua field harus diisi", Snackbar.LENGTH_LONG).show()
            return
        }

        val laporanId = databaseLaporan.push().key
        if (laporanId != null) {
            val laporan = Laporan(laporanId, nama, telepon, lokasi, tanggal, isi, fotoUrl)
            databaseLaporan.child(laporanId).setValue(laporan)
                .addOnCompleteListener {
                    progressDialog.dismiss()
                    if (it.isSuccessful) {
                        Snackbar.make(fabSend, "Laporan berhasil dikirim", Snackbar.LENGTH_LONG).show()
                        navigateToDetailLaporan(laporanId)
                    } else {
                        Snackbar.make(fabSend, "Gagal mengirim laporan", Snackbar.LENGTH_LONG).show()
                    }
                }
        } else {
            progressDialog.dismiss()
            Snackbar.make(fabSend, "Gagal membuat ID laporan", Snackbar.LENGTH_LONG).show()
        }

    }

    private fun navigateToDetailLaporan(laporanId: String) {
        val intent = Intent(this, LaporanDetail::class.java)
        intent.putExtra("LAPORAN_ID", laporanId)
        startActivity(intent)
        finish()
    }
}
