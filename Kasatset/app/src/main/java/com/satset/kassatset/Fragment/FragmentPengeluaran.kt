package com.satset.kassatset.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.ManajemenWarga.EditWargaActivity
import com.satset.kassatset.ManajemenWarga.InputPengeluaran
import com.satset.kassatset.ManajemenWarga.Pengeluaran
import com.satset.kassatset.PengeluaranWargaAdapter
import com.satset.kassatset.databinding.FragmentPengeluaranBinding

class FragmentPengeluaran : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var pengeluaranList: MutableList<Pengeluaran>
    private lateinit var pengeluaranAdapter: PengeluaranWargaAdapter
    private var _binding: FragmentPengeluaranBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPengeluaranBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("pengeluaran")
        pengeluaranList = mutableListOf()
        pengeluaranAdapter = PengeluaranWargaAdapter(pengeluaranList)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = pengeluaranAdapter

        fetchPengeluaranData()

        binding.tambahPengeluaran.setOnClickListener {
            val intent = Intent(requireContext(), InputPengeluaran::class.java)
            startActivity(intent)
        }
    }

    private fun fetchPengeluaranData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pengeluaranList.clear()
                var totalPengeluaran = 0.0
                for (pengeluaranSnapshot in snapshot.children) {
                    val pengeluaran = pengeluaranSnapshot.getValue(Pengeluaran::class.java)
                    pengeluaran?.let {
                        pengeluaranList.add(it)
                        totalPengeluaran += it.totalPengeluaran?.toDoubleOrNull() ?: 0.0
                    }
                }
                pengeluaranAdapter.notifyDataSetChanged()
                // Menampilkan total pengeluaran di TextView
                binding.totalPengeluaran.text = "Total Pengeluaran\nRp. $totalPengeluaran"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
