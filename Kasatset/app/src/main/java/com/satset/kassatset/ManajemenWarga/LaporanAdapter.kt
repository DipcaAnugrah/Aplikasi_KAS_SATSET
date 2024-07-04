package com.satset.kassatset.ManajemenWarga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satset.kassatset.Laporan.Laporan
import com.satset.kassatset.R
import com.satset.kassatset.databinding.ItemReportManajemenBinding

class LaporanAdapter(
    private val laporanList: List<Laporan>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<LaporanAdapter.LaporanViewHolder>() {

    inner class LaporanViewHolder(private val binding: ItemReportManajemenBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(laporan: Laporan) {
            // Tampilkan foto warga dengan ukuran 100dp x 100dp
            Glide.with(binding.root)
                .load(laporan.fotoBukti)
                .placeholder(R.drawable.placeholder_image)
                .override(100, 100) // Atur ukuran gambar menjadi 100dp x 100dp
                .into(binding.imageLaporan)

            binding.namaPelapor.text = laporan.namaPelapor
            binding.teleponPelapor.text = laporan.teleponPelapor
            binding.lokasiKejadian.text = laporan.lokasiKejadian
            binding.isiLaporan.text = laporan.isiLaporan
            binding.tanggalKejadian.text = laporan.tanggalKejadian?.toString()


            binding.buttonHapus.setOnClickListener {
                listener.onDeleteClick(laporan)
            }

            binding.imageLaporan.setOnClickListener {
                if (binding.root.context is FragmentActivity) {
                    val activity = binding.root.context as FragmentActivity
                    val dialogFragment = ImageDialogFragment.newInstance(laporan.fotoBukti)
                    dialogFragment.show(activity.supportFragmentManager, "imageDialog")
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LaporanViewHolder {
        val binding = ItemReportManajemenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LaporanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LaporanViewHolder, position: Int) {
        holder.bind(laporanList[position])
    }

    override fun getItemCount(): Int {
        return laporanList.size
    }

    interface OnItemClickListener {
        fun onDeleteClick(laporan: Laporan)
    }
}
