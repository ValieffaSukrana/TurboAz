package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ItemSelectedRcycleBinding

class SelectedAdapter : ListAdapter<FavoriteCar, SelectedAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(val binding: ItemSelectedRcycleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSelectedRcycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = getItem(position)
        holder.binding.apply {
            // Maşın adı
            SelectedItemText.text = "${favorite.brand} ${favorite.model}"

            // Şəkil yüklə
            Glide.with(root.context)
                .load(favorite.url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(carImageView)

            // Həmişə dolu ürək (çünki favorite-dədir)
            favoriteIcon.setImageResource(R.drawable.filled_heart)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<FavoriteCar>() {
        override fun areItemsTheSame(oldItem: FavoriteCar, newItem: FavoriteCar): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FavoriteCar, newItem: FavoriteCar): Boolean {
            return oldItem == newItem
        }
    }
}