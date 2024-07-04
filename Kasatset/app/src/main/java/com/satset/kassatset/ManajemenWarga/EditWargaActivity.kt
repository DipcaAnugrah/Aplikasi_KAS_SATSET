package com.satset.kassatset.ManajemenWarga

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satset.kassatset.databinding.ActivityEditIuranBinding
import java.util.*

class EditWargaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditIuranBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var wargaId: String
    private var calendar = Calendar.getInstance()
    private var warga: Warga? = null
    private var imageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditIuranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("warga")
        // Inisialisasi Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference.child("warga_images")

        // Mendapatkan ID warga yang diedit dari intent
        wargaId = intent.getStringExtra("WARGA_ID") ?: ""

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Edit Warga"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menyimpan perubahan...")
        progressDialog.setCancelable(false)

        // Mendapatkan data warga dari Firebase berdasarkan ID
        database.child(wargaId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                warga = dataSnapshot.getValue(Warga::class.java)
                warga?.let {
                    // Menampilkan data warga ke dalam EditText
                    binding.namaLengkap.setText(it.namaLengkap)
                    binding.jumlahIuran.setText(it.jumlahIuran.toString())
                    binding.statusPembayaran.setText(it.statusKeaktifan)
                    binding.tanggalPembayaran.setText(it.tanggalPembayaran)

                    // Tampilkan foto warga sebelumnya menggunakan Glide
                    if (!it.urlFoto.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(it.urlFoto)
                            .into(binding.imageWarga)
                    }
                }
            } else {
                Toast.makeText(this, "Warga tidak ditemukan", Toast.LENGTH_SHORT).show()
                finish()
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, "Gagal mendapatkan data warga: ${exception.message}", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Setup button click listeners
        binding.buttonBatal.setOnClickListener {
            finish()
        }

        binding.buttonSelesai.setOnClickListener {
            // Simpan perubahan ke Firebase
            simpanPerubahan()
        }

        // Setup click listener for tanggalPembayaran
        binding.tanggalPembayaran.setOnClickListener {
            tampilkanDatePicker()
        }

        // Setup click listener for imageLaporan
        binding.imageWarga.setOnClickListener {
            // Open gallery to choose image
            pickImageFromGallery()
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
            binding.imageWarga.setImageURI(imageUri)
        }
    }

    private fun simpanPerubahan() {
        progressDialog.show()
        val namaWarga = binding.namaLengkap.text.toString().trim()
        val jumlahIuran = binding.jumlahIuran.text.toString().trim().toIntOrNull() ?: warga?.jumlahIuran ?: 0
        val statusPembayaran = binding.statusPembayaran.text.toString().trim()
        val tanggalPembayaran = binding.tanggalPembayaran.text.toString().trim()

        // Validasi input
        if (namaWarga.isEmpty()) {
            binding.namaLengkap.error = "Nama warga tidak boleh kosong"
            return
        }

        // Update objek Warga dengan data yang baru
        warga?.apply {
            namaLengkap = namaWarga
            this.jumlahIuran = jumlahIuran
            statusKeaktifan = statusPembayaran
            this.tanggalPembayaran = tanggalPembayaran
        }

        // Upload new image to Firebase Storage if available
        if (imageUri != null) {
            val imageName = System.currentTimeMillis().toString() + ".jpg"
            val imageReference = storageReference.child(imageName)

            imageReference.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // If image upload is successful, get image URL
                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        warga?.urlFoto = imageUrl // Update the image URL in the Warga object

                        // Save updated Warga object to Firebase Database
                        warga?.let {
                            database.child(wargaId).setValue(it).addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Data warga berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Gagal menyimpan perubahan: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Gagal mengunggah gambar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // If no new image is selected, save updated Warga object to Firebase Database
            warga?.let {
                database.child(wargaId).setValue(it).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Data warga berhasil diperbarui", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this, "Gagal menyimpan perubahan: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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
                val formattedDate = android.text.format.DateFormat.format("yyyy-MM-dd", selectedDate).toString()
                binding.tanggalPembayaran.setText(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }
}
