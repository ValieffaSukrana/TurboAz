package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemModelRecycleBinding

class ModelAdapter(
    private val models: List<String>,
    private val onModelClick: (String) -> Unit
) : RecyclerView.Adapter<ModelAdapter.ModelViewHolder>() {

    private var filteredModels = models.toList()
    private var selectedPosition = -1 // ✅ Seçilmiş pozisiya

    inner class ModelViewHolder(private val binding: ItemModelRecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: String, position: Int) {
            binding.brandName.text = model

            // ✅ RadioButton state
            binding.radioButton.isChecked = (position == selectedPosition)

            // ✅ Root click
            binding.root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = adapterPosition

                // Əvvəlki seçimi uncheck et
                if (previousPosition != -1) {
                    notifyItemChanged(previousPosition)
                }
                // Yeni seçimi check et
                notifyItemChanged(selectedPosition)

                // Callback çağır
                onModelClick(model)
            }

            // ✅ RadioButton click (root click ilə eyni)
            binding.radioButton.setOnClickListener {
                binding.root.performClick()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModelViewHolder {
        val binding = ItemModelRecycleBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ModelViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ModelViewHolder, position: Int) {
        holder.bind(filteredModels[position], position)
    }

    override fun getItemCount() = filteredModels.size

    fun filter(query: String) {
        filteredModels = if (query.isEmpty()) {
            models
        } else {
            models.filter { it.contains(query, ignoreCase = true) }
        }
        selectedPosition = -1 // Reset selection
        notifyDataSetChanged()
    }
}