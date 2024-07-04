package com.satset.kassatset.ManajemenWarga

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.databinding.ActivityWargaBinding

class WargaActivity : AppCompatActivity(), WargaAdapter.OnItemClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var wargaList: MutableList<Warga>
    private lateinit var wargaAdapter: WargaAdapter
    private lateinit var binding: ActivityWargaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWargaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("warga")
        wargaList = mutableListOf()
        wargaAdapter = WargaAdapter(wargaList, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = wargaAdapter

        fetchWargaData()

        binding.tambahWarga.setOnClickListener {
            val intent = Intent(this, TambahWargaActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchWargaData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                wargaList.clear()
                for (wargaSnapshot in snapshot.children) {
                    val warga = wargaSnapshot.getValue(Warga::class.java)
                    warga?.let {
                        wargaList.add(it)
                    }
                }
                wargaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Tangani kesalahan jika ada
            }
        })
    }

    override fun onEditClick(warga: Warga) {
        val intent = Intent(this, EditWargaActivity::class.java)
        intent.putExtra("WARGA_ID", warga.id)
        startActivity(intent)
    }

    override fun onDeleteClick(warga: Warga) {
        database.child(warga.id!!).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
