package uz.smartarena.edu.iu.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.smartarena.edu.R
import uz.smartarena.edu.databinding.ItemBookBinding
import uz.smartarena.edu.model.Subjects
import uz.smartarena.edu.utils.DiffUtils
import uz.smartarena.edu.utils.inflate

class SubjectAdapter : ListAdapter<Subjects, SubjectAdapter.VH>(DiffUtils<Subjects>()) {
    private lateinit var listener: (String) -> Unit
    fun setListener(block: (String) -> Unit) {
        listener = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemBookBinding.bind(view)
        fun bind(item: Subjects) {
            binding.name.text = item.name
            binding.image.setImageResource(item.image)
            binding.devideText.text = "${item.doneThemes}/${item.countOfThemes}"
            binding.textAcceptance.text = item.acceptance.toString()
            binding.root.setOnClickListener { listener.invoke(item.name) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent.inflate(R.layout.item_book))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(currentList[position])
}