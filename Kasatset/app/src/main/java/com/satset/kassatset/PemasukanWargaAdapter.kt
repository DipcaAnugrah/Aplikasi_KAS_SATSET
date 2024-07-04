package com.satset.kassatset.ManajemenWarga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.satset.kassatset.databinding.ItemPemasukanWargaBinding

class PemasukanWargaAdapter(
    private val pemasukanList: List<Pemasukan>
) : RecyclerView.Adapter<PemasukanWargaAdapter.PemasukanViewHolder>() {

    inner class PemasukanViewHolder(private val binding: ItemPemasukanWargaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pemasukan: Pemasukan) {
            binding.apply {
                jenisPemasukan1.text = pemasukan.jenisPemasukan1
                pemasukan1.text = pemasukan.pemasukan1
                jenisPemasukan2.text = pemasukan.jenisPemasukan2
                pemasukan2.text = pemasukan.pemasukan2
                jenisPemasukan3.text = pemasukan.jenisPemasukan3
                pemasukan3.text = pemasukan.pemasukan3
                totalPemasukan.text = pemasukan.totalPemasukan
                itemTanggal.text = pemasukan.tanggalPemasukan
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PemasukanViewHolder {
        val binding = ItemPemasukanWargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PemasukanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PemasukanViewHolder, position: Int) {
        holder.bind(pemasukanList[position])
    }

    override fun getItemCount(): Int {
        return pemasukanList.size
    }
}
