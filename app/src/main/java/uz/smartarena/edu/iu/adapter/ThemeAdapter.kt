package uz.smartarena.edu.iu.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.smartarena.edu.R
import uz.smartarena.edu.databinding.ItemThemeBinding
import uz.smartarena.edu.utils.DiffUtils
import uz.smartarena.edu.utils.inflate

class ThemeAdapter : ListAdapter<String, ThemeAdapter.VH>(DiffUtils<String>()) {
    private lateinit var listener: (String) -> Unit
    fun setListener(block: (String) -> Unit) {
        listener = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemThemeBinding.bind(view)
        fun bind(item: String) {
            binding.name.text = item
            binding.root.setOnClickListener { listener.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent.inflate(R.layout.item_theme))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(currentList[position])
}