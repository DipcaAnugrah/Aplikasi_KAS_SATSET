package com.satset.kassatset.ManajemenWarga

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.satset.kassatset.R
import com.satset.kassatset.databinding.ActivityDialogFragmentBinding

class ImageDialogFragment : DialogFragment() {

    private var _binding: ActivityDialogFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = ActivityDialogFragmentBinding.inflate(layoutInflater)

        val imageUrl = arguments?.getString("imageUrl")
        Glide.with(requireContext())
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(binding.imageView)

        return Dialog(requireContext()).apply {
            setContentView(binding.root)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(imageUrl: String): ImageDialogFragment {
            val args = Bundle().apply {
                putString("imageUrl", imageUrl)
            }
            return ImageDialogFragment().apply {
                arguments = args
            }
        }
    }
}
