package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemBrandRecycleBinding


class BrandAdapter(
    private val brands: List<String>,
    private val onBrandClick: (String) -> Unit
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    private var filteredBrands = brands.toList()

    inner class BrandViewHolder(private val binding: ItemBrandRecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: String) {
            binding.brandName.text = brand
            binding.root.setOnClickListener {
                onBrandClick(brand)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val binding = ItemBrandRecycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BrandViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        holder.bind(filteredBrands[position])
    }

    override fun getItemCount() = filteredBrands.size

    fun filter(query: String) {
        filteredBrands = if (query.isEmpty()) {
            brands
        } else {
            brands.filter { it.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}