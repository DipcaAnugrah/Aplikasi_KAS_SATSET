package com.satset.kassatset.ManajemenWarga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satset.kassatset.R
import com.satset.kassatset.databinding.ItemWargaBinding

class WargaAdapter(
    private val wargaList: List<Warga>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<WargaAdapter.WargaViewHolder>() {

    inner class WargaViewHolder(private val binding: ItemWargaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(warga: Warga) {
            // Tampilkan foto warga dengan ukuran 100dp x 100dp
            Glide.with(binding.root)
                .load(warga.urlFoto)
                .placeholder(R.drawable.placeholder_image)
                .override(100, 100) // Atur ukuran gambar menjadi 100dp x 100dp
                .into(binding.fotoWarga)

            binding.namaWarga.text = warga.namaLengkap
            binding.alamat.text = warga.alamat
            binding.statusKeaktifan.text = warga.statusKeaktifan
            binding.jumlahIuran.text = warga.jumlahIuran?.toString() // Konversi ke String
            binding.tanggalPembayaran.text = warga.tanggalPembayaran?.toString()

            binding.buttonEdit.setOnClickListener {
                listener.onEditClick(warga)
            }

            binding.buttonHapus.setOnClickListener {
                listener.onDeleteClick(warga)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WargaViewHolder {
        val binding = ItemWargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WargaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WargaViewHolder, position: Int) {
        holder.bind(wargaList[position])
    }

    override fun getItemCount(): Int {
        return wargaList.size
    }

    interface OnItemClickListener {
        fun onEditClick(warga: Warga)
        fun onDeleteClick(warga: Warga)
    }
}
