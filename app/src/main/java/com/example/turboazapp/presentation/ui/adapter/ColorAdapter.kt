package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.turboazapp.data.local.ColorModel
import com.example.turboazapp.databinding.ItemColorBinding

class ColorAdapter()
//    private val colorList: List<ColorModel>,
//    private val onColorClick: (ColorModel) -> Unit
//
//) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): ColorViewHolder {
//        val binding = ItemColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return ColorViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
//        val color = colorList[position]
//        holder.colorName.text = color.colorName
//        holder.itemView.setOnClickListener {
//            onColorClick(color)
//        }
//
//    }
//
//    override fun getItemCount(): Int {
//        return colorList.size
//    }
//
//    inner class ColorViewHolder(binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root) {
//        private val colorCircle = binding.colorCircle
//        val colorName = binding.colorName
//    }
//
//}