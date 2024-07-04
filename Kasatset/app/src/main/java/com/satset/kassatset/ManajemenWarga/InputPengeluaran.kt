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
import com.satset.kassatset.databinding.InputPengeluaranBinding
import com.satset.kassatset.ManajemenWarga.Pengeluaran
import java.util.Calendar

class InputPengeluaran : AppCompatActivity() {

    private lateinit var binding: InputPengeluaranBinding

    private lateinit var databasePengeluaran: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = InputPengeluaranBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database
        databasePengeluaran = FirebaseDatabase.getInstance().getReference("pengeluaran")

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this).apply {
            setMessage("Menambah Pengeluaran...")
            setCancelable(false)
        }

        // Set up DatePickerDialog for tanggalPengeluaran
        binding.tanggalPengeluaran.setOnClickListener {
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
                binding.tanggalPengeluaran.setText(selectedDate)
            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun setupTextChangeListeners() {
        val pengeluaranEditTexts = arrayOf(
            binding.pengeluaran1,
            binding.pengeluaran2,
            binding.pengeluaran3,
            binding.pengeluaran4
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
        val pengeluaran1 = binding.pengeluaran1.text.toString().toDoubleOrNull() ?: 0.0
        val pengeluaran2 = binding.pengeluaran2.text.toString().toDoubleOrNull() ?: 0.0
        val pengeluaran3 = binding.pengeluaran3.text.toString().toDoubleOrNull() ?: 0.0
        val pengeluaran4 = binding.pengeluaran4.text.toString().toDoubleOrNull() ?: 0.0

        val totalPengeluaran = pengeluaran1 + pengeluaran2 + pengeluaran3 + pengeluaran4
        binding.totalPengeluaran.setText(totalPengeluaran.toString())
    }

    private fun sendPengeluaran() {
        val pengeluaran1 = binding.pengeluaran1.text.toString().trim()
        val jenisPengeluaran1 = binding.jenisPengeluaran.text.toString().trim()
        val pengeluaran2 = binding.pengeluaran2.text.toString().trim()
        val jenisPengeluaran2 = binding.jenisPengeluaran2.text.toString().trim()
        val pengeluaran3 = binding.pengeluaran3.text.toString().trim()
        val jenisPengeluaran3 = binding.jenisPengeluaran3.text.toString().trim()
        val pengeluaran4 = binding.pengeluaran4.text.toString().trim()
        val jenisPengeluaran4 = binding.jenisPengeluaran4.text.toString().trim()
        val totalPengeluaran = binding.totalPengeluaran.text.toString().trim()
        val tanggalPengeluaran = binding.tanggalPengeluaran.text.toString().trim()

        if (pengeluaran1.isEmpty() || totalPengeluaran.isEmpty() || tanggalPengeluaran.isEmpty()) {
            Snackbar.make(binding.buttonSelesai, "Semua field harus diisi", Snackbar.LENGTH_LONG).show()
            return
        }

        val idPengeluaran = databasePengeluaran.push().key ?: ""
        val pengeluaran = Pengeluaran(
            idPengeluaran,
            pengeluaran1,
            jenisPengeluaran1,
            pengeluaran2,
            jenisPengeluaran2,
            pengeluaran3,
            jenisPengeluaran3,
            pengeluaran4,
            jenisPengeluaran4,
            totalPengeluaran,
            tanggalPengeluaran
        )

        databasePengeluaran.child(idPengeluaran).setValue(pengeluaran)
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
