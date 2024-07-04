package com.satset.kassatset

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.ManajemenWarga.Pengumuman
import com.satset.kassatset.databinding.ActivityPengumumanManajemenBinding
import com.satset.kassatset.databinding.ActivityPengumumanWargaBinding

class PengumumanWarga : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var pengumumanList: MutableList<Pengumuman>
    private lateinit var pengumumanAdapter: PengumumanWargaAdapter
    private lateinit var binding: ActivityPengumumanWargaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengumumanWargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("pengumuman")
        pengumumanList = mutableListOf()
        pengumumanAdapter = PengumumanWargaAdapter(pengumumanList)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = pengumumanAdapter

        fetchPengumumanData()
    }

    private fun fetchPengumumanData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pengumumanList.clear()
                for (pengumumanSnapshot in snapshot.children) {
                    val pengumuman = pengumumanSnapshot.getValue(Pengumuman::class.java)
                    pengumuman?.let {
                        pengumumanList.add(it)
                    }
                }
                pengumumanAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@PengumumanWarga, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
