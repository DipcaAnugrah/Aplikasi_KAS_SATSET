package com.satset.kassatset

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.satset.kassatset.Fragment2.FragmentMain2
import com.satset.kassatset.Laporan.LaporanActivity
import com.satset.kassatset.ManajemenWarga.MenuManajemen
import com.satset.kassatset.ManajemenWarga.WargaActivity

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Find the CardView elements by their IDs
        val cvWarga: CardView = findViewById(R.id.cvWarga)
        val cvKas: CardView = findViewById(R.id.cvKas)
        val cvPengumuman: CardView = findViewById(R.id.cvPengumuman)
        val cvLaporan: CardView = findViewById(R.id.cvLaporan)
        val cvManajemenWarga: CardView = findViewById(R.id.cvManajemenWarga)

        // Menghilangkan status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Set click listeners for each CardView
        cvWarga.setOnClickListener {
            Toast.makeText(this, "Daftar Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        cvKas.setOnClickListener {
            Toast.makeText(this, "Laporan KAS Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, FragmentMain2::class.java)
            startActivity(intent)
        }

        cvPengumuman.setOnClickListener {
            Toast.makeText(this, "Pengumuman Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, PengumumanWarga::class.java)
            startActivity(intent)
        }

        cvLaporan.setOnClickListener {
            Toast.makeText(this, "Laporan Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, LaporanActivity::class.java)
            startActivity(intent)
        }

        cvManajemenWarga.setOnClickListener {
            Toast.makeText(this, "Manajemen Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
