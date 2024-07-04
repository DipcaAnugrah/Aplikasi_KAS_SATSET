package com.satset.kassatset.Fragment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.ManajemenWarga.Pemasukan
import com.satset.kassatset.ManajemenWarga.PemasukanWargaAdapter
import com.satset.kassatset.databinding.FragmentPemasukanWargaBinding

class FragmentPemasukan2 : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var pemasukanList: MutableList<Pemasukan>
    private lateinit var pemasukanAdapter: PemasukanWargaAdapter
    private var _binding: FragmentPemasukanWargaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPemasukanWargaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("pemasukan")
        pemasukanList = mutableListOf()
        pemasukanAdapter = PemasukanWargaAdapter(pemasukanList)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = pemasukanAdapter

        fetchPemasukanData()
    }

    private fun fetchPemasukanData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pemasukanList.clear()
                var totalPemasukan = 0.0
                for (pemasukanSnapshot in snapshot.children) {
                    val pemasukan = pemasukanSnapshot.getValue(Pemasukan::class.java)
                    pemasukan?.let {
                        pemasukanList.add(it)
                        totalPemasukan += it.totalPemasukan?.toDoubleOrNull() ?: 0.0
                    }
                }
                pemasukanAdapter.notifyDataSetChanged()
                // Menampilkan total pemasukan di TextView
                binding.totalPemasukan.text = "Total Pemasukan\nRp. $totalPemasukan"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
