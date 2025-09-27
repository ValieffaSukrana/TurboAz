package com.example.turboazapp.presentation.ui.adapter

import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.turboazapp.R
import com.example.turboazapp.databinding.ItemHomeRcycleBinding
import com.example.turboazapp.presentation.ui.model.ImageList

class HomeAdapter(private val homeList: List<ImageList>) :
    RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemHomeRcycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = homeList[position]
        holder.bind(item)

    }

    override fun getItemCount(): Int {
        return homeList.size
    }

    inner class ViewHolder(val binding: ItemHomeRcycleBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(item: ImageList) {
            binding.itemText.text = item.text
            Glide.with(binding.root.context)
                .load(item.imageResId)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(binding.carImageView)
        }


    }


}