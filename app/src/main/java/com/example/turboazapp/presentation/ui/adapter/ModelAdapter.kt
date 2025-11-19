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

    inner class ModelViewHolder(private val binding: ItemModelRecycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(model: String) {
            binding.brandName.text = model
            binding.root.setOnClickListener {
                onModelClick(model)
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
        holder.bind(filteredModels[position])
    }

    override fun getItemCount() = filteredModels.size

    fun filter(query: String) {
        filteredModels = if (query.isEmpty()) {
            models
        } else {
            models.filter { it.contains(query, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }
}