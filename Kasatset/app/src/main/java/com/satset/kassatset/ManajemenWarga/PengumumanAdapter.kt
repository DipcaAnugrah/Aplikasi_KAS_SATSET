package com.satset.kassatset.ManajemenWarga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satset.kassatset.R
import com.satset.kassatset.databinding.ItemPengumumanManajemenBinding

class PengumumanAdapter(
    private val pengumumanList: List<Pengumuman>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PengumumanAdapter.PengumumanViewHolder>() {

    inner class PengumumanViewHolder(private val binding: ItemPengumumanManajemenBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pengumuman: Pengumuman) {
            // Tampilkan foto pengumuman dengan ukuran 100dp x 100dp
            Glide.with(binding.root)
                .load(pengumuman.urlFoto)  // Correct property name
                .placeholder(R.drawable.placeholder_image)
                .override(100, 100) // Atur ukuran gambar menjadi 100dp x 100dp
                .into(binding.imagePengumuman)

            binding.judul.text = pengumuman.judul
            binding.isiPengumuman.text = pengumuman.isiPengumuman
            binding.tanggalPengumuman.text = pengumuman.tanggalPengumuman.toString()

            binding.imagePengumuman.setOnClickListener {
                if (binding.root.context is FragmentActivity) {
                    val activity = binding.root.context as FragmentActivity
                    val dialogFragment = ImageDialogFragment.newInstance(pengumuman.urlFoto)
                    dialogFragment.show(activity.supportFragmentManager, "imageDialog")
                }
            }

            binding.buttonEdit.setOnClickListener {
                listener.onEditClick(pengumuman)
            }

            binding.buttonHapus.setOnClickListener {
                listener.onDeleteClick(pengumuman)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengumumanViewHolder {
        val binding = ItemPengumumanManajemenBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PengumumanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PengumumanViewHolder, position: Int) {
        holder.bind(pengumumanList[position])
    }

    override fun getItemCount(): Int {
        return pengumumanList.size
    }

    interface OnItemClickListener {
        fun onEditClick(pengumuman: Pengumuman)
        fun onDeleteClick(pengumuman: Pengumuman)
    }
}
