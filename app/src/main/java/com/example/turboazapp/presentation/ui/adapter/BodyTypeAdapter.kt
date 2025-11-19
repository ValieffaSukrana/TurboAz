package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemBodyTypeBinding

class BodyTypeAdapter(
    private val bodyTypes: List<String>,
    private val onBodyTypeClick: (String) -> Unit
) : RecyclerView.Adapter<BodyTypeAdapter.BodyTypeViewHolder>() {

    inner class BodyTypeViewHolder(private val binding: ItemBodyTypeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(bodyType: String) {
            binding.bodyTypeName.text = bodyType
            binding.root.setOnClickListener {
                onBodyTypeClick(bodyType)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BodyTypeViewHolder {
        val binding = ItemBodyTypeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BodyTypeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BodyTypeViewHolder, position: Int) {
        holder.bind(bodyTypes[position])
    }

    override fun getItemCount() = bodyTypes.size
}