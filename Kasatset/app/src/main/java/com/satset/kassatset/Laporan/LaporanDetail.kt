package com.satset.kassatset.Laporan


import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.satset.kassatset.R
import com.bumptech.glide.Glide
import com.google.firebase.database.*
import com.satset.kassatset.databinding.ActivityDetailReportBinding

class LaporanDetail : AppCompatActivity() {

    private lateinit var binding: ActivityDetailReportBinding
    private lateinit var databaseLaporan: DatabaseReference

    private lateinit var namaPelapor: TextView
    private lateinit var teleponPelapor: TextView
    private lateinit var lokasiKejadian: TextView
    private lateinit var tanggalKejadian: TextView
    private lateinit var isiLaporan: TextView
    private lateinit var imageLaporan: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Database reference
        databaseLaporan = FirebaseDatabase.getInstance().getReference("laporan")

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Detail Laporan Warga"
            setDisplayHomeAsUpEnabled(true)
        }
        // Initialize views
        namaPelapor = findViewById(R.id.namaPelapor)
        teleponPelapor = findViewById(R.id.teleponPelapor)
        lokasiKejadian = findViewById(R.id.lokasiKejadian)
        tanggalKejadian = findViewById(R.id.tanggalKejadian)
        isiLaporan = findViewById(R.id.isiLaporan)
        imageLaporan = findViewById(R.id.imageLaporan)

        // Retrieve report ID from intent extras
        val reportId = intent.getStringExtra("LAPORAN_ID") ?: return

        // Fetch and display the report details
        fetchReportDetails(reportId)
    }

    private fun fetchReportDetails(reportId: String) {
        databaseLaporan.child(reportId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val laporan = snapshot.getValue(Laporan::class.java)
                    laporan?.let {
                        displayReportDetails(it)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun displayReportDetails(laporan: Laporan) {
        namaPelapor.text = laporan.namaPelapor
        teleponPelapor.text = laporan.teleponPelapor
        lokasiKejadian.text = laporan.lokasiKejadian
        tanggalKejadian.text = laporan.tanggalKejadian
        isiLaporan.text = laporan.isiLaporan
        if (laporan.fotoBukti.isNotEmpty()) {
            Glide.with(this).load(laporan.fotoBukti).into(imageLaporan)
        } else {
            imageLaporan.setImageResource(R.drawable.ic_image_upload)
        }
    }
}
