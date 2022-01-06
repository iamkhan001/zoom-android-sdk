package sg.mirobotic.zoom.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sg.mirobotic.zoom.data.TextInfo
import sg.mirobotic.zoom.databinding.ItemTextInfoBinding

class TextInfoAdapter(private val list: ArrayList<TextInfo>): RecyclerView.Adapter<TextInfoAdapter.MyViewHolder>() {

    class MyViewHolder(view: ItemTextInfoBinding) : RecyclerView.ViewHolder(view.root) {
        val binding: ItemTextInfoBinding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemTextInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = list[position]

        val binding = holder.binding

        binding.title.text = item.title
        binding.info.text = item.info

    }

    override fun getItemCount(): Int = list.size
}