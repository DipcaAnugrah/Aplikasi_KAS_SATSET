package com.satset.kassatset

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.satset.kassatset.ManajemenWarga.ImageDialogFragment
import com.satset.kassatset.ManajemenWarga.Warga

class DaftarAdapter(private val listWarga: List<Warga>) : RecyclerView.Adapter<DaftarAdapter.WargaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WargaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daftar, parent, false)
        return WargaViewHolder(view)
    }

    override fun onBindViewHolder(holder: WargaViewHolder, position: Int) {
        val currentWarga = listWarga[position]
        holder.bind(currentWarga)
    }

    override fun getItemCount(): Int {
        return listWarga.size
    }

    inner class WargaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaWargaTextView: TextView = itemView.findViewById(R.id.nama_warga)
        private val alamatTextView: TextView = itemView.findViewById(R.id.alamat)
        private val emailTextView: TextView = itemView.findViewById(R.id.email)
        private val nomorTeleponTextView: TextView = itemView.findViewById(R.id.nomor_telepon)
        private val jumlahIuranTextView: TextView = itemView.findViewById(R.id.jumlah_iuran)
        private val tanggalPembayaranTextView: TextView = itemView.findViewById(R.id.tanggal_pembayaran)
        private val fotoWargaImageView: ImageView = itemView.findViewById(R.id.foto_warga)

        fun bind(warga: Warga) {
            namaWargaTextView.text = warga.namaLengkap
            alamatTextView.text = warga.alamat
            emailTextView.text = warga.email
            nomorTeleponTextView.text = warga.nomorTelepon
            jumlahIuranTextView.text = warga.jumlahIuran.toString()
            tanggalPembayaranTextView.text = warga.tanggalPembayaran

            // Load image using Glide
            Glide.with(itemView)
                .load(warga.urlFoto)
                .placeholder(R.drawable.placeholder_image) // Placeholder image while loading
                .error(R.drawable.error_image) // Error image if loading fails
                .into(fotoWargaImageView)

            fotoWargaImageView.setOnClickListener {
                if (itemView.context is FragmentActivity) {
                    val activity = itemView.context as FragmentActivity
                    val dialogFragment = ImageDialogFragment.newInstance(warga.urlFoto ?: "")
                    dialogFragment.show(activity.supportFragmentManager, "imageDialog")
                }
            }
        }
    }
}
