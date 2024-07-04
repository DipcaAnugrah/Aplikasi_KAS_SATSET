package com.satset.kassatset.ManajemenWarga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.satset.kassatset.databinding.ItemPengeluaranBinding

class PengeluaranManajemenAdapter(
    private val pengeluaranList: List<Pengeluaran>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PengeluaranManajemenAdapter.PengeluaranViewHolder>() {

    inner class PengeluaranViewHolder(private val binding: ItemPengeluaranBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onEditClick(pengeluaranList[position])
                }
            }

            binding.buttonHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(pengeluaranList[position])
                }
            }
        }

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
        val binding = ItemPengeluaranBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PengeluaranViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PengeluaranViewHolder, position: Int) {
        holder.bind(pengeluaranList[position])
    }

    override fun getItemCount(): Int {
        return pengeluaranList.size
    }
    interface OnItemClickListener {
        fun onEditClick(pengeluaran: Pengeluaran)
        fun onDeleteClick(pengeluaran: Pengeluaran)
    }
}
