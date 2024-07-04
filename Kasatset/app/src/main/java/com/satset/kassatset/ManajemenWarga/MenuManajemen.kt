package com.satset.kassatset.ManajemenWarga

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.satset.kassatset.Fragment.FragmentMain
import com.satset.kassatset.Laporan.LaporanActivity
import com.satset.kassatset.LogoutActivity
import com.satset.kassatset.R


class MenuManajemen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_manajemen)

        // Find the CardView elements by their IDs
        val cvWarga: CardView = findViewById(R.id.cvWarga)
        val cvPengumuman: CardView = findViewById(R.id.cvPengumuman)
        val cvLaporanKas: CardView = findViewById(R.id.cvLaporanKas)
        val cvLaporanWarga: CardView = findViewById(R.id.cvLaporanWarga)
        val cvLogout: CardView = findViewById(R.id.cvLogout)

        // Menghilangkan status bar
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        // Set click listeners for each CardView
        cvWarga.setOnClickListener {
            Toast.makeText(this, "Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, WargaActivity::class.java)
            startActivity(intent)
        }

        cvPengumuman.setOnClickListener {
            Toast.makeText(this, "Pengumuman Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, PengumumanActivity::class.java)
            startActivity(intent)
        }

        cvLaporanKas.setOnClickListener {
            Toast.makeText(this, "Laporan Kas clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, FragmentMain::class.java)
            startActivity(intent)
        }

        cvLaporanWarga.setOnClickListener {
            Toast.makeText(this, "Laporan Warga clicked", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, LaporanManajemen::class.java)
            startActivity(intent)
        }

        cvLogout.setOnClickListener {
            Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()
            // Add your code here to handle the click event, e.g., start a new Activity
            val intent = Intent(this, LogoutActivity::class.java)
            startActivity(intent)
        }

    }
}
