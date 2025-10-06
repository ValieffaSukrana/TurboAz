package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ItemHomeRcycleBinding
import com.example.turboazapp.domain.model.Car

class HomeAdapter(
    private val onFavoriteClick: (Car, Boolean) -> Unit = { _, _ -> }
) : ListAdapter<Car, HomeAdapter.ViewHolder>(DiffCallback()) {

    private val favoriteItems = mutableSetOf<String>()

    inner class ViewHolder(val binding: ItemHomeRcycleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHomeRcycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val car = getItem(position)
        holder.binding.apply {
            // Maşın adı
            itemText.text = "${car.brand} ${car.model}"

            // Şəkil yüklə
            Glide.with(root.context)
                .load(car.url)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(carImageView)

            // Favorite state
            val isFavorite = favoriteItems.contains(car.id)
            updateFavoriteIcon(holder, isFavorite)

            // Favorite icon click
            favoriteIcon.setOnClickListener {
                val newState = !favoriteItems.contains(car.id)

                if (newState) {
                    favoriteItems.add(car.id)
                } else {
                    favoriteItems.remove(car.id)
                }

                updateFavoriteIcon(holder, newState)
                onFavoriteClick(car, newState)
            }
        }
    }

    private fun updateFavoriteIcon(holder: ViewHolder, isFavorite: Boolean) {
        if (isFavorite) {
            holder.binding.favoriteIcon.setImageResource(R.drawable.filled_heart)
        } else {
            holder.binding.favoriteIcon.setImageResource(R.drawable.empty_heart)
        }
    }

    fun setFavoriteItems(favorites: Set<String>) {
        favoriteItems.clear()
        favoriteItems.addAll(favorites)
        notifyDataSetChanged()
    }

    // updateList funksiyası - ListAdapter-də submitList istifadə edirik
    fun updateList(newList: List<Car>) {
        submitList(newList)
    }

    // DiffUtil callback
    class DiffCallback : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }
    }
}