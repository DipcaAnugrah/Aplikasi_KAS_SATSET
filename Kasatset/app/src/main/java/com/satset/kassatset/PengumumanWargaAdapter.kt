package com.satset.kassatset

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satset.kassatset.ManajemenWarga.ImageDialogFragment
import com.satset.kassatset.ManajemenWarga.Pengumuman
import com.satset.kassatset.databinding.ItemPengumumanWargaBinding

class PengumumanWargaAdapter(
    private val pengumumanList: List<Pengumuman>
) : RecyclerView.Adapter<PengumumanWargaAdapter.PengumumanWargaViewHolder>() {

    inner class PengumumanWargaViewHolder(private val binding: ItemPengumumanWargaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(pengumuman: Pengumuman) {
            // Display the announcement photo with a size of 100dp x 100dp
            Glide.with(binding.root)
                .load(pengumuman.urlFoto)  // Correct property name
                .placeholder(R.drawable.placeholder_image)
                .override(100, 100) // Set image size to 100dp x 100dp
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
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PengumumanWargaViewHolder {
        val binding = ItemPengumumanWargaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PengumumanWargaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PengumumanWargaViewHolder, position: Int) {
        holder.bind(pengumumanList[position])
    }

    override fun getItemCount(): Int {
        return pengumumanList.size
    }
}
