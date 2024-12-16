package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemImageSelectionBinding
import com.google.firebase.storage.StorageReference
import coil.load
import coil.transform.RoundedCornersTransformation

class ImageSelectionAdapter(
    private val imageRefs: List<StorageReference>,
    private val onImageSelected: (StorageReference) -> Unit
) : RecyclerView.Adapter<ImageSelectionAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemImageSelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageRef: StorageReference) {
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                binding.imageView.load(uri) {
                    crossfade(true)
                    transformations(RoundedCornersTransformation(8f))
                }
            }

            itemView.setOnClickListener {
                onImageSelected(imageRef)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageSelectionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageRefs[position])
    }

    override fun getItemCount() = imageRefs.size
}