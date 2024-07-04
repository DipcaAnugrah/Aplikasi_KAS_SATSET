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
import com.satset.kassatset.ManajemenWarga.InputPemasukan
import com.satset.kassatset.ManajemenWarga.Pemasukan
import com.satset.kassatset.ManajemenWarga.PemasukanManajemenAdapter
import com.satset.kassatset.databinding.FragmentPemasukanBinding

class FragmentPemasukan : Fragment(), PemasukanManajemenAdapter.OnItemClickListener {

    private lateinit var database: DatabaseReference
    private lateinit var pemasukaList: MutableList<Pemasukan>
    private lateinit var pemasukanAdapter: PemasukanManajemenAdapter
    private var _binding: FragmentPemasukanBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPemasukanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance().getReference("pemasukan")
        pemasukaList = mutableListOf()
        pemasukanAdapter = PemasukanManajemenAdapter(pemasukaList, this)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = pemasukanAdapter

        fetchWargaData()

        binding.tambahPemasukan.setOnClickListener {
            val intent = Intent(requireContext(), InputPemasukan::class.java)
            startActivity(intent)
        }
    }

    private fun fetchWargaData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pemasukaList.clear()
                var totalPemasukan = 0.0
                for (pemasukanSnapshot in snapshot.children) {
                    val pemasukan = pemasukanSnapshot.getValue(Pemasukan::class.java)
                    pemasukan?.let {
                        pemasukaList.add(it)
                        totalPemasukan += it.totalPemasukan?.toDoubleOrNull() ?: 0.0
                    }
                }
                pemasukanAdapter.notifyDataSetChanged()
                // Menampilkan total pemasukan di TextView
                binding.totalPemasukan.text = "Total Pemasukan\nRp. $totalPemasukan"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
                Toast.makeText(requireContext(), "Gagal mengambil data: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onEditClick(pemasukan: Pemasukan) {
        val intent = Intent(requireContext(), EditWargaActivity::class.java)
        intent.putExtra("PEMASUKAN-ID", pemasukan.id)
        startActivity(intent)
    }

    override fun onDeleteClick(pemasukan: Pemasukan) {
        database.child(pemasukan.id!!).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Data berhasil dihapus", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Gagal menghapus data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
