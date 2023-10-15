package uz.smartarena.edu.iu.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.smartarena.edu.R
import uz.smartarena.edu.data.local.EncryptedLocalStorage
import uz.smartarena.edu.databinding.ItemThemeBinding
import uz.smartarena.edu.model.ThemeData
import uz.smartarena.edu.utils.DiffUtils
import uz.smartarena.edu.utils.inflate

class ThemeAdapter : ListAdapter<ThemeData, ThemeAdapter.VH>(DiffUtils<ThemeData>()) {
    private lateinit var listener: (String) -> Unit
    private var storage = EncryptedLocalStorage.getInstance()
    fun setListener(block: (String) -> Unit) {
        listener = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemThemeBinding.bind(view)
        fun bind(item: ThemeData) {
            if (storage.current <= adapterPosition) {
                binding.star.imageTintList = ColorStateList.valueOf(Color.GRAY)
                binding.processText.apply {
                    text = "Ruxsat berilmagan"
                    setTextColor(resources.getColor(R.color.seriy))
                }
                binding.icon.setImageResource(R.drawable.deny)
            } else {
                binding.root.setOnClickListener {
                    listener.invoke(item.theme)
                }
            }
            binding.number.text = "${adapterPosition + 1}-mavzu"
            if (item.acceptance == 0.0) {
                binding.textAcceptance.text = "_._"
                if (storage.current == adapterPosition + 1) {
                    binding.icon.setImageResource(R.drawable.pause)
                    binding.processText.apply {
                        text = "O'zlashtirilmoqda"
                        setTextColor(resources.getColor(R.color.main))
                    }
                }
            } else {
                if (item.acceptance <= 3.5) {
                    binding.star.imageTintList = ColorStateList.valueOf(Color.RED)
                    binding.processText.apply {
                        text = "Qayta topshirish"
                        setTextColor(resources.getColor(R.color.red))
                    }
                    binding.icon.setImageResource(R.drawable.redo)
                } else {
                    binding.star.imageTintList = ColorStateList.valueOf(Color.YELLOW)
                }
                binding.textAcceptance.text = item.acceptance.toString()
            }
            binding.theme.text = item.theme
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent.inflate(R.layout.item_theme))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(currentList[position])
}