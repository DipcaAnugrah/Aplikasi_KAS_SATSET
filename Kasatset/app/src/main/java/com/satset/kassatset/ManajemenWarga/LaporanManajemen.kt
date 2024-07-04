package com.satset.kassatset.ManajemenWarga

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.Laporan.Laporan
import com.satset.kassatset.databinding.ActivityReportManajemenBinding

class LaporanManajemen : AppCompatActivity(), LaporanAdapter.OnItemClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var laporanList: MutableList<Laporan>
    private lateinit var laporanAdapter: LaporanAdapter
    private lateinit var binding: ActivityReportManajemenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportManajemenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("laporan")
        laporanList = mutableListOf()
        laporanAdapter = LaporanAdapter(laporanList, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = laporanAdapter

        fetchLaporanData()

    }

    private fun fetchLaporanData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                laporanList.clear()
                for (laporanSnapshot in snapshot.children) {
                    val laporan = laporanSnapshot.getValue(Laporan::class.java)
                    laporan?.let {
                        laporanList.add(it)
                    }
                }
                laporanAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani kesalahan jika ada
            }
        })
    }

    override fun onDeleteClick(laporan: Laporan) {
        database.child(laporan.id!!).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
