package com.example.turboazapp.presentation.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemBrandRecycleBinding

class BrandAdapter(
    private val brands: List<String>,
    private val onBrandClick: (String) -> Unit
) : RecyclerView.Adapter<BrandAdapter.BrandViewHolder>() {

    private var filteredBrands = brands.toList()
    private var selectedPosition = -1

    inner class BrandViewHolder(private val binding: ItemBrandRecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(brand: String, position: Int) {
            binding.brandName.text = brand
            binding.radioButton.isChecked = (position == selectedPosition)

            // ✅ Root click - ƏN ƏSAS HİSSƏ
            binding.root.setOnClickListener {
                Log.d("BrandAdapter", "Click: $brand at position $position") // DEBUG

                // Artıq seçilibsə, təkrar seçilməsin
                if (selectedPosition == adapterPosition) {
                    Log.d("BrandAdapter", "Already selected, ignoring") // DEBUG
                    return@setOnClickListener
                }

                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                // UI yenilə
                if (previousPosition != -1) {
                    notifyItemChanged(previousPosition)
                }
                notifyItemChanged(selectedPosition)

                Log.d("BrandAdapter", "Calling callback for: $brand") // DEBUG

                // ✅ Callback çağır
                onBrandClick(brand)
            }

            // RadioButton click (root-a yönləndir)
            binding.radioButton.setOnClickListener {
                binding.root.performClick()
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
        holder.bind(filteredBrands[position], position)
    }

    override fun getItemCount() = filteredBrands.size

    fun filter(query: String) {
        filteredBrands = if (query.isEmpty()) {
            brands
        } else {
            brands.filter { it.contains(query, ignoreCase = true) }
        }
        selectedPosition = -1
        notifyDataSetChanged()
    }
}