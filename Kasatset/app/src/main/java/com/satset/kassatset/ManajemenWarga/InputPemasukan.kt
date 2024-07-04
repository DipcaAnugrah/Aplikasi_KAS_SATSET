package com.satset.kassatset.ManajemenWarga

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.satset.kassatset.databinding.InputPemasukanBinding
import com.satset.kassatset.ManajemenWarga.Pemasukan
import java.util.Calendar

class InputPemasukan : AppCompatActivity() {

    private lateinit var binding: InputPemasukanBinding

    private lateinit var databasePemasukan: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InputPemasukanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database and Storage
        databasePemasukan = FirebaseDatabase.getInstance().getReference("pemasukan")

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Menambah Pemasukan...")
            setCancelable(false)
        }

        // Set up DatePickerDialog for tanggalPengeluaran
        binding.tanggalPemasukan.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up text change listeners for pengeluaran fields
        setupTextChangeListeners()

        // Set up form submission
        binding.buttonSelesai.setOnClickListener {
            sendPengeluaran()
        }

        // Set up cancel button
        binding.buttonBatal.setOnClickListener {
            onBackPressed()
        }
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
                binding.tanggalPemasukan.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setupTextChangeListeners() {
        val pengeluaranEditTexts = arrayOf(
            binding.pemasukan1,
            binding.pemasukan2,
            binding.pemasukan3
        )

        for (editText in pengeluaranEditTexts) {
            editText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    // No implementation needed
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    // No implementation needed
                }

                override fun afterTextChanged(s: Editable?) {
                    hitungTotalPengeluaran()
                }
            })
        }
    }

    private fun hitungTotalPengeluaran() {
        val pengeluaran1 = binding.pemasukan1.text.toString().toDoubleOrNull() ?: 0.0
        val pengeluaran2 = binding.pemasukan2.text.toString().toDoubleOrNull() ?: 0.0
        val pengeluaran3 = binding.pemasukan3.text.toString().toDoubleOrNull() ?: 0.0

        val totalPengeluaran = pengeluaran1 + pengeluaran2 + pengeluaran3
        binding.totalPemasukan.setText(totalPengeluaran.toString())
    }

    private fun sendPengeluaran() {
        val pemasukan1 = binding.pemasukan1.text.toString().trim()
        val jenisPemasukan1 = binding.jenisPemasukan1.text.toString().trim()
        val pemasukan2 = binding.pemasukan2.text.toString().trim()
        val jenisPemasukan2 = binding.jenisPemasukan2.text.toString().trim()
        val pemasukan3 = binding.pemasukan3.text.toString().trim()
        val jenisPemasukan3 = binding.jenisPemasukan3.text.toString().trim()
        val totalPemasukan = binding.totalPemasukan.text.toString().trim()
        val tanggalPemasukan = binding.tanggalPemasukan.text.toString().trim()

        if (pemasukan1.isEmpty() || jenisPemasukan1.isEmpty() || totalPemasukan.isEmpty() || tanggalPemasukan.isEmpty()) {
            Snackbar.make(binding.buttonSelesai, "Semua field harus diisi", Snackbar.LENGTH_LONG).show()
            return
        }

        val idPengeluaran = databasePemasukan.push().key ?: ""
        val pengeluaran = Pemasukan(
            idPengeluaran,
            pemasukan1,
            jenisPemasukan1,
            pemasukan2,
            jenisPemasukan2,
            pemasukan3,
            jenisPemasukan3,
            totalPemasukan,
            tanggalPemasukan
        )

        databasePemasukan.child(idPengeluaran).setValue(pengeluaran)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Snackbar.make(binding.buttonSelesai, "Pengeluaran berhasil ditambahkan", Snackbar.LENGTH_LONG).show()
                    onBackPressed()
                } else {
                    Snackbar.make(binding.buttonSelesai, "Gagal menambahkan pengeluaran", Snackbar.LENGTH_LONG).show()
                }
            }
    }
}
