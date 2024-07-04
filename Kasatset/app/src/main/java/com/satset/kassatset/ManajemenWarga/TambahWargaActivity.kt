package com.satset.kassatset.ManajemenWarga

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.format.DateFormat
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satset.kassatset.databinding.ActivityTambahWargaBinding
import java.util.Calendar

class TambahWargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTambahWargaBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private var imageUri: Uri? = null
    private var calendar = Calendar.getInstance()
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahWargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("warga")
        // Inisialisasi Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference.child("warga_images")

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Tambah Warga"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menambah data warga...")
        progressDialog.setCancelable(false)

        // Setup button click listeners
        binding.buttonBatal.setOnClickListener {
            finish()
        }

        binding.buttonSelesai.setOnClickListener {
            // Tambahkan warga baru ke Firebase
            tambahWargaBaru()
        }

        // Setup click listener for image upload
        binding.imageLaporan.setOnClickListener {
            // Open gallery to choose image
            pickImageFromGallery()
        }

        // Setup click listener for tanggalPembayaran
        binding.tanggalPembayaran.setOnClickListener {
            tampilkanDatePicker()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            binding.imageLaporan.setImageURI(imageUri)
        }
    }

    private fun tambahWargaBaru() {
        // Menampilkan ProgressDialog sebelum memulai proses
        progressDialog.show()

        // Retrieve input values
        val namaLengkap = binding.namaLengkap.text.toString().trim()
        val jenisKelamin = binding.jenisKelamin.text.toString().trim()
        val alamat = binding.alamat.text.toString().trim()
        val nomorTelepon = binding.nomorTelepon.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val statusPernikahan = binding.statusPernikahan.text.toString().trim()
        val statusKeaktifan = binding.statusKeaktifan.text.toString().trim()
        val pekerjaan = binding.pekerjaan.text.toString().trim()
        val namaPasangan = binding.namaPasangan.text.toString().trim()
        val jumlahAnak = binding.jumlahAnak.text.toString().trim().toIntOrNull() ?: 0
        val jumlahIuran = binding.jumlahIuran.text.toString().trim().toIntOrNull() ?: 0
        val tanggalPembayaran = binding.tanggalPembayaran.text.toString().trim()

        // Validasi input
        if (namaLengkap.isEmpty() ||
            jenisKelamin.isEmpty() ||
            alamat.isEmpty() ||
            nomorTelepon.isEmpty()||
            email.isEmpty() ||
            statusPernikahan.isEmpty() ||
            statusKeaktifan.isEmpty() ||
            pekerjaan.isEmpty()
        ) {
            when {
                namaLengkap.isEmpty() -> showError("Nama lengkap harus diisi")
                jenisKelamin.isEmpty() -> showError("Jenis kelamin harus diisi")
                alamat.isEmpty() -> showError("Alamat harus diisi")
                nomorTelepon.isEmpty() -> showError("Nomor telepon harus diisi")
                email.isEmpty() -> showError("Email harus diisi")
                statusPernikahan.isEmpty() -> showError("Status pernikahan harus diisi")
                statusKeaktifan.isEmpty() -> showError("Status keaktifan harus diisi")
                pekerjaan.isEmpty() -> showError("Pekerjaan harus diisi")
            }
            progressDialog.dismiss() // Menghilangkan ProgressDialog karena proses gagal
            return
        }


        // Buat ID baru untuk warga
        val wargaId = database.push().key ?: return

        // Check if an image is selected
        if (imageUri != null) {
            val imageName = System.currentTimeMillis().toString() + ".jpg"
            val imageReference = storageReference.child(imageName)

            imageReference.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // If image upload is successful, get image URL
                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()

                        // Create new Warga object with image URL
                        val wargaBaru = Warga(
                            id = wargaId,
                            namaLengkap = namaLengkap,
                            jenisKelamin = jenisKelamin,
                            alamat = alamat,
                            nomorTelepon = nomorTelepon,
                            email = email,
                            statusPernikahan = statusPernikahan,
                            statusKeaktifan = statusKeaktifan,
                            pekerjaan = pekerjaan,
                            namaPasangan = namaPasangan,
                            jumlahAnak = jumlahAnak,
                            jumlahIuran = jumlahIuran,
                            urlFoto = imageUrl, // Set the image URL
                            tanggalPembayaran = tanggalPembayaran
                        )

                        // Save to Firebase Database
                        database.child(wargaId).setValue(wargaBaru)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    showToast("Warga baru berhasil ditambahkan")
                                } else {
                                    showError("Gagal menambahkan warga: ${task.exception?.message}")
                                }
                                // Menyembunyikan ProgressDialog setelah proses selesai
                                progressDialog.dismiss()
                                finish()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    showError("Gagal mengunggah gambar: ${e.message}")
                    progressDialog.dismiss() // Menghilangkan ProgressDialog karena proses gagal
                }
        } else {
            showError("Unggah foto warga")
            progressDialog.dismiss() // Menghilangkan ProgressDialog karena proses gagal
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun tampilkanDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                // Set selected date to EditText
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                val formattedDate = DateFormat.format("yyyy-MM-dd", selectedDate).toString()
                binding.tanggalPembayaran.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
