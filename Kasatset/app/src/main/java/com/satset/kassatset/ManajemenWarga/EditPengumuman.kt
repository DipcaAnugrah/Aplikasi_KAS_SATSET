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
import com.satset.kassatset.databinding.ActivityEditPengumumanBinding
import java.util.*

class EditPengumuman : AppCompatActivity() {

    private lateinit var binding: ActivityEditPengumumanBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var pengumumanId: String
    private var calendar = Calendar.getInstance()
    private var pengumuman: Pengumuman? = null
    private var imageUri: Uri? = null
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPengumumanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi Firebase Database
        database = FirebaseDatabase.getInstance().reference.child("pengumuman")
        // Inisialisasi Firebase Storage
        storageReference = FirebaseStorage.getInstance().reference.child("pengumuman_images")

        // Mendapatkan ID warga yang diedit dari intent
        pengumumanId = intent.getStringExtra("PENGUMUMAN_ID") ?: ""

        // Setup toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Edit Pengumuman"
            setDisplayHomeAsUpEnabled(true)
        }

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Menyimpan perubahan...")
        progressDialog.setCancelable(false)

        // Mendapatkan data warga dari Firebase berdasarkan ID
        database.child(pengumumanId).get().addOnSuccessListener { dataSnapshot ->
            if (dataSnapshot.exists()) {
                pengumuman = dataSnapshot.getValue(Pengumuman::class.java)
                pengumuman?.let {
                    // Menampilkan data warga ke dalam EditText
                    binding.judul.setText(it.judul)
                    binding.tanggalPengumuman.setText(it.tanggalPengumuman)
                    binding.isiPengumuman.setText(it.isiPengumuman.toString())

                    // Tampilkan foto warga sebelumnya menggunakan Glide
                    if (!it.urlFoto.isNullOrEmpty()) {
                        Glide.with(this)
                            .load(it.urlFoto)
                            .into(binding.imagePengumuman)
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
        binding.tanggalPengumuman.setOnClickListener {
            tampilkanDatePicker()
        }

        // Setup click listener for imageLaporan
        binding.imagePengumuman.setOnClickListener {
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
            binding.imagePengumuman.setImageURI(imageUri)
        }
    }

    private fun simpanPerubahan() {
        progressDialog.show()
        val judul = binding.judul.text.toString().trim()
        val tanggalPengumuman = binding.tanggalPengumuman.text.toString().trim()
        val isiPengumuman = binding.isiPengumuman.text.toString().trim()

        // Validasi input
        if (judul.isEmpty() || tanggalPengumuman.isEmpty() || isiPengumuman.isEmpty()) {
            Toast.makeText(this, "Semua bidang harus diisi", Toast.LENGTH_SHORT).show()
            progressDialog.dismiss()
            return
        }

        // Update objek pengumuman dengan data yang baru
        pengumuman?.apply {
            this.judul = judul
            this.tanggalPengumuman = tanggalPengumuman
            this.isiPengumuman = isiPengumuman
        }

        // Upload new image to Firebase Storage if available
        if (imageUri != null) {
            val imageName = "${System.currentTimeMillis()}.jpg"
            val imageReference = storageReference.child(imageName)

            imageReference.putFile(imageUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    // If image upload is successful, get image URL
                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        val imageUrl = uri.toString()
                        pengumuman?.urlFoto = imageUrl // Update the image URL in the Pengumuman object

                        // Save updated Pengumuman object to Firebase Database
                        pengumuman?.let {
                            database.child(pengumumanId).setValue(it).addOnCompleteListener { task ->
                                progressDialog.dismiss()
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Data pengumuman berhasil diperbarui", Toast.LENGTH_SHORT).show()
                                    finish()
                                } else {
                                    Toast.makeText(this, "Gagal menyimpan perubahan: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this, "Gagal mengunggah gambar: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            // If no new image is selected, save updated Pengumuman object to Firebase Database
            pengumuman?.let {
                database.child(pengumumanId).setValue(it).addOnCompleteListener { task ->
                    progressDialog.dismiss()
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Data pengumuman berhasil diperbarui", Toast.LENGTH_SHORT).show()
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
                binding.tanggalPengumuman.setText(formattedDate)
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
