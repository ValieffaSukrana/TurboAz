package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ItemSelectedRcycleBinding
import com.example.turboazapp.domain.model.Car

class SelectedAdapter(
    private val onItemClick: (Car) -> Unit,
    private val onFavoriteClick: (Car, Boolean) -> Unit
) : RecyclerView.Adapter<SelectedAdapter.SelectedViewHolder>() {

    private var carList = mutableListOf<Car>()
    private var favoriteItems = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedViewHolder {
        val binding = ItemSelectedRcycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SelectedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedViewHolder, position: Int) {
        holder.bind(carList[position])
    }

    override fun getItemCount(): Int = carList.size

    fun updateList(newList: List<Car>) {
        carList.clear()
        carList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setFavoriteItems(favorites: Set<String>) {
        favoriteItems = favorites.toMutableSet()
        notifyDataSetChanged()
    }

    inner class SelectedViewHolder(
        private val binding: ItemSelectedRcycleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            // Mətn göstər
            binding.SelectedItemText.text = "${car.brand} ${car.model}"
            binding.SelectedItemPrice.text = "${car.price} AZN"


            // Şəkil yüklə
            if (car.images.isNotEmpty()) {
                Glide.with(binding.root.context)
                    .load(car.images.first())
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(binding.carImageView)
            } else {
                binding.carImageView.setImageResource(R.drawable.ic_launcher_background)
            }

            // Favorite icon göstər
            val isFavorite = favoriteItems.contains(car.id)
            binding.favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.filled_heart else R.drawable.empty_heart
            )

            // Click listeners
            binding.root.setOnClickListener {
                onItemClick(car)
            }

            binding.favoriteIcon.setOnClickListener {
                val newFavoriteState = !isFavorite
                if (newFavoriteState) {
                    favoriteItems.add(car.id)
                } else {
                    favoriteItems.remove(car.id)
                }
                notifyItemChanged(adapterPosition)
                onFavoriteClick(car, isFavorite)
            }
        }
    }
}