package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemSelectedRcycleBinding

class SelectedAdapter(val selectedList: List<String>) : RecyclerView.Adapter<SelectedAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding= ItemSelectedRcycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = selectedList[position]
        holder.binding.SelectedItemText.text = item
    }

    override fun getItemCount(): Int {
        return selectedList.size
    }
    inner class ViewHolder(val binding: ItemSelectedRcycleBinding): RecyclerView.ViewHolder(binding.root) {
    }
    }

