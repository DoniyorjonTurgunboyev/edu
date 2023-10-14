package uz.smartarena.edu.iu.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.smartarena.edu.R
import uz.smartarena.edu.utils.DiffUtils
import uz.smartarena.edu.utils.inflate

class LocationAdapter : ListAdapter<String, LocationAdapter.VH>(DiffUtils<String>()) {
    private lateinit var listener: (String) -> Unit
    fun setListener(block: (String) -> Unit) {
        listener = block
    }

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val text: TextView = view.findViewById(R.id.location)
        fun bind(item: String) {
            text.text = item
            itemView.setOnClickListener { listener.invoke(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(parent.inflate(R.layout.item_locations))

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(currentList[position])
}