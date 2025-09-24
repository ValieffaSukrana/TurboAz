package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemHomeRcycleBinding

class HomeAdapter(private val homeList: List<String>): RecyclerView.Adapter<HomeAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemHomeRcycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = homeList[position]
        holder.binding.itemText.text = item

    }

    override fun getItemCount(): Int {
       return homeList.size
    }
    inner class ViewHolder(val binding: ItemHomeRcycleBinding): RecyclerView.ViewHolder(binding.root) {

    }

}