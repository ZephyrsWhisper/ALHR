package zephyr.ALHR

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class Adapter(var lama: List<Lama>, val selectListener: (position:Int) -> Unit) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_info_cell, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int = lama.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lama[position]
        holder.bind(item)

        holder.itemView.setOnLongClickListener{
            selectListener(position)
            true
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.cellText)
        private val imageView: ImageView = itemView.findViewById(R.id.iconView)

        fun bind(item: Lama) {
            nameTextView.text = item.properName
            if (item.species == "Alpaca") imageView.setImageResource(R.drawable.alpaca)
            if (item.species == "Llama") imageView.setImageResource(R.drawable.llama)
        }
    }


}