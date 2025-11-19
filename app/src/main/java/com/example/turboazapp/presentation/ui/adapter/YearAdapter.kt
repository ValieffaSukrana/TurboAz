package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemYearBinding

class YearAdapter(
    private val years: List<Int>,
    private val onYearClick: (Int) -> Unit
) : RecyclerView.Adapter<YearAdapter.YearViewHolder>() {

    inner class YearViewHolder(private val binding: ItemYearBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(year: Int) {
            binding.yearText.text = year.toString()
            binding.root.setOnClickListener {
                onYearClick(year)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val binding = ItemYearBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return YearViewHolder(binding)
    }

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        holder.bind(years[position])
    }

    override fun getItemCount() = years.size
}