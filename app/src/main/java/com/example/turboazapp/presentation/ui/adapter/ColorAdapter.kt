package com.example.turboazapp.presentation.ui.adapter
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.turboazapp.databinding.ItemColorBinding
import com.example.turboazapp.presentation.ui.fragment.announcement.ColorItem

class ColorAdapter(
    private val colors: List<ColorItem>,
    private val onColorClick: (ColorItem) -> Unit
) : RecyclerView.Adapter<ColorAdapter.ColorViewHolder>() {

    inner class ColorViewHolder(private val binding: ItemColorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(colorItem: ColorItem) {
            binding.colorName.text = colorItem.name

            try {
                val color = Color.parseColor(colorItem.hexCode)
                binding.colorCircle.setCardBackgroundColor(color)

                // Ağ və açıq rənglər üçün stroke daha görünən et
                if (colorItem.hexCode == "#FFFFFF" ||
                    colorItem.hexCode == "#FFFF00" ||
                    colorItem.hexCode == "#F5F5DC" ||
                    colorItem.hexCode == "#FFC0CB") {
                    binding.colorCircle.strokeWidth = 3
                    binding.colorCircle.strokeColor = Color.parseColor("#CCCCCC")
                } else {
                    binding.colorCircle.strokeWidth = 1
                    binding.colorCircle.strokeColor = Color.parseColor("#E0E0E0")
                }
            } catch (e: IllegalArgumentException) {
                binding.colorCircle.setCardBackgroundColor(Color.WHITE)
                binding.colorCircle.strokeWidth = 2
                binding.colorCircle.strokeColor = Color.parseColor("#CCCCCC")
            }

            binding.root.setOnClickListener {
                onColorClick(colorItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder {
        val binding = ItemColorBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ColorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.bind(colors[position])
    }

    override fun getItemCount() = colors.size
}