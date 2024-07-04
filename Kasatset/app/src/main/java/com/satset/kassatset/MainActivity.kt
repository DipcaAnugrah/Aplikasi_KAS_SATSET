package com.satset.kassatset

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.satset.kassatset.ManajemenWarga.Warga

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wargaAdapter: DaftarAdapter
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rv_users)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        wargaAdapter = DaftarAdapter(emptyList())
        recyclerView.adapter = wargaAdapter

        databaseReference = FirebaseDatabase.getInstance().reference.child("warga")
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val listWarga = mutableListOf<Warga>()
                    for (wargaSnapshot in snapshot.children) {
                        val warga = wargaSnapshot.getValue(Warga::class.java)
                        warga?.let {
                            listWarga.add(it)
                        }
                    }
                    wargaAdapter = DaftarAdapter(listWarga)
                    recyclerView.adapter = wargaAdapter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
}
