package com.satset.kassatset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.satset.kassatset.ManajemenWarga.Pengeluaran
import com.satset.kassatset.databinding.ItemPengeluaranWargaBinding

class PengeluaranWargaAdapter(
    private val pengeluaranList: List<Pengeluaran>
) : RecyclerView.Adapter<PengeluaranWargaAdapter.PengeluaranViewHolder>() {

    inner class PengeluaranViewHolder(private val binding: ItemPengeluaranWargaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pengeluaran: Pengeluaran) {
            binding.apply {
                jenisPengeluaran1.text = pengeluaran.jenisPengeluaran1
                pengeluaran1.text = pengeluaran.pengeluaran1
                jenisPengeluaran2.text = pengeluaran.jenisPengeluaran2
                pengeluaran2.text = pengeluaran.pengeluaran2
                jenisPengeluaran3.text = pengeluaran.jenisPengeluaran3
                pengeluaran3.text = pengeluaran.pengeluaran3
                jenisPengeluaran4.text = pengeluaran.jenisPengeluaran4
                pengeluaran4.text = pengeluaran.pengeluaran4
                totalPengeluaran.text = pengeluaran.totalPengeluaran
                itemTanggal.text = pengeluaran.tanggalPengeluaran
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengeluaranViewHolder {
        val binding = ItemPengeluaranWargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PengeluaranViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PengeluaranViewHolder, position: Int) {
        holder.bind(pengeluaranList[position])
    }

    override fun getItemCount(): Int {
        return pengeluaranList.size
    }
}
