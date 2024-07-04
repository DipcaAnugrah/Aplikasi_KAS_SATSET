package com.satset.kassatset.ManajemenWarga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.satset.kassatset.databinding.ItemPemasukanBinding

class PemasukanManajemenAdapter(
    private val pemasukanList: List<Pemasukan>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PemasukanManajemenAdapter.PemasukanViewHolder>() {

    inner class PemasukanViewHolder(private val binding: ItemPemasukanBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonEdit.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(pemasukanList[position])
                }
            }

            binding.buttonHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(pemasukanList[position])
                }
            }
        }

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
        val binding = ItemPemasukanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PemasukanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PemasukanViewHolder, position: Int) {
        holder.bind(pemasukanList[position])
    }

    override fun getItemCount(): Int {
        return pemasukanList.size
    }

    interface OnItemClickListener {
        fun onEditClick(pemasukan: Pemasukan)
        fun onDeleteClick(pemasukan: Pemasukan)
    }
}
