package com.example.turboazapp.presentation.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.target.Target
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ItemHomeRcycleBinding
import com.example.turboazapp.domain.model.Car




class HomeAdapter(
    private val onItemClick: (Car) -> Unit,
    private val onFavoriteClick: (Car, Boolean) -> Unit
) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    companion object {
        private const val TAG = "HomeAdapter"
    }

    private var carList = mutableListOf<Car>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeRcycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(carList[position])
    }

    override fun getItemCount(): Int = carList.size

    fun updateList(newList: List<Car>) {
        Log.d(TAG, "updateList called with ${newList.size} cars")

        val favoriteCars = newList.filter { it.isFavorite }
        Log.d(TAG, "Cars with isFavorite=true: ${favoriteCars.size}")
        favoriteCars.forEach {
            Log.d(TAG, "  - ${it.id} is favorite")
        }

        carList.clear()
        carList.addAll(newList)
        notifyDataSetChanged()
    }

    fun setFavoriteItems(favorites: Set<String>) {
        // DEPRECATED - artıq istifadə edilmir
    }

    inner class HomeViewHolder(
        private val binding: ItemHomeRcycleBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            // Mətn
            binding.carName.text = "${car.brand} ${car.model}"
            binding.carPrice.text = "${car.price} AZN"

            // Şəkil
            if (car.images.isNotEmpty()) {
                val imageUrl = car.images.first()

                try {
                    Glide.with(binding.root.context)
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_launcher_background)
                        .error(R.drawable.ic_launcher_background)
                        .into(binding.carImageView)

                } catch (e: Exception) {
                }
            } else {
                binding.carImageView.setImageResource(R.drawable.ic_launcher_background)
            }


            // Favorite icon
            val isFavorite = car.isFavorite
            Log.d(TAG, "Binding car ${car.id}: isFavorite=$isFavorite")

            binding.favoriteIcon.setImageResource(
                if (isFavorite) R.drawable.filled_heart else R.drawable.empty_heart
            )

            // Clicks
            binding.root.setOnClickListener {
                onItemClick(car)
            }

            binding.favoriteIcon.setOnClickListener {
                Log.d(TAG, "Favorite clicked for ${car.id}: current=$isFavorite")
                onFavoriteClick(car, isFavorite)
            }
        }
    }
}