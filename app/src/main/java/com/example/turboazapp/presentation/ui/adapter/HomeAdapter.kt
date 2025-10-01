package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ItemHomeRcycleBinding
import com.example.turboazapp.presentation.ui.model.MakeWithImage

class HomeAdapter(private var makeList: List<MakeWithImage> = emptyList()) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeRcycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = makeList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return makeList.size
    }

    // API-dən data gələndə list-i yeniləmək üçün
    fun updateList(newList: List<MakeWithImage>) {
        makeList = newList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemHomeRcycleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MakeWithImage) {
            binding.itemText.text = item.name
            Glide.with(binding.root.context)
                .load(item.imageRes)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.carImageView)
        }
    }
}