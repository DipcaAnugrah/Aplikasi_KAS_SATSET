package com.satset.kassatset.Fragment2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.satset.kassatset.ManajemenWarga.Pengeluaran
import com.satset.kassatset.PengeluaranWargaAdapter
import com.satset.kassatset.databinding.FragmentPengeluaranWargaBinding

class FragmentPengeluaran2 : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var pengeluaranList: MutableList<Pengeluaran>
    private lateinit var pengeluaranAdapter: PengeluaranWargaAdapter
    private var _binding: FragmentPengeluaranWargaBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPengeluaranWargaBinding.inflate(inflater, container, false)
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
        fetchTotalPengeluaran()
    }

    private fun fetchPengeluaranData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pengeluaranList.clear()
                for (pengeluaranSnapshot in snapshot.children) {
                    val pengeluaran = pengeluaranSnapshot.getValue(Pengeluaran::class.java)
                    pengeluaran?.let {
                        pengeluaranList.add(it)
                    }
                }
                pengeluaranAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
                Toast.makeText(requireContext(), "Failed to fetch data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchTotalPengeluaran() {
        database.child("totalPengeluaran").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val totalPengeluaran = snapshot.getValue(Long::class.java) ?: 0
                binding.totalPengeluaran.text = "Total Pengeluaran\nRp. $totalPengeluaran"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
                Toast.makeText(requireContext(), "Failed to fetch total pengeluaran: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
