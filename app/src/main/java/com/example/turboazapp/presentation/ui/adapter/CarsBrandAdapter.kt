package com.example.turboazapp.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.turboazapp.databinding.ItemBrandRecycleBinding

class CarsBrandAdapter(

)
//    :
//    RecyclerView.Adapter<CarsBrandAdapter.CarBrandViewHolder>() {
//    override fun onCreateViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): CarBrandViewHolder {
//        val binding =
//            ItemBrandRecycleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return CarBrandViewHolder(binding)
//    }

//    override fun onBindViewHolder(holder: CarBrandViewHolder, position: Int) {
//        val brand = brands[position]
//        holder.binding.brandName.text = brand.name
//        holder.binding.brandLogo.setImageResource(brand.logoRes)
//        holder.binding.radioButton.setOnClickListener {
//            onClick(brand)
//        }



//
//    override fun getItemCount(): Int {
//        return brands.size
//    }
//
//    inner class CarBrandViewHolder(val binding: ItemBrandRecycleBinding) :
//        RecyclerView.ViewHolder(binding.root)
////        fun bind(item: CarBrandsModel) {
////            binding.brandName.text = item.name
////            binding.brandLogo.setImageResource(item.logoRes)
//////            binding.radioButton.isChecked = item.isSelected
////
//////            binding.radioButton.setOnClickListener {
//////                brands.forEach { it.isSelected = false }
//////                item.isSelected = true
//////                notifyDataSetChanged()
//////                onClick(item) // Fragment-ə xəbər verir
//////            }
////
////            // Eyni funksiyanı root click də çağırır
////            binding.root.setOnClickListener {
////                binding.radioButton.performClick()
////            }
//}