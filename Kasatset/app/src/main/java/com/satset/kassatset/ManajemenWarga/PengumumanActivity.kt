package com.satset.kassatset.ManajemenWarga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.databinding.ActivityPengumumanManajemenBinding

class PengumumanActivity : AppCompatActivity(), PengumumanAdapter.OnItemClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var pengumumanList: MutableList<Pengumuman>
    private lateinit var pengumumanAdapter: PengumumanAdapter
    private lateinit var binding: ActivityPengumumanManajemenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPengumumanManajemenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("pengumuman")
        pengumumanList = mutableListOf()
        pengumumanAdapter = PengumumanAdapter(pengumumanList, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = pengumumanAdapter

        fetchPengumumanData()

        binding.buatPengumuman.setOnClickListener {
            val intent = Intent(this, TambahPengumuman::class.java)
            startActivity(intent)
        }
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
                Toast.makeText(this@PengumumanActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onEditClick(pengumuman: Pengumuman) {
        val intent = Intent(this, EditPengumuman::class.java)
        intent.putExtra("PENGUMUMAN_ID", pengumuman.id)
        startActivity(intent)
    }

    override fun onDeleteClick(pengumuman: Pengumuman) {
        database.child(pengumuman.id!!).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
