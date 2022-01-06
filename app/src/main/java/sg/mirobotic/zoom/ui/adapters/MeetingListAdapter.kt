package sg.mirobotic.zoom.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sg.mirobotic.zoom.data.Meeting
import sg.mirobotic.zoom.databinding.ItemMeetingBinding
import sg.mirobotic.zoom.utils.toShowTime

class MeetingListAdapter(private val onItemSelectListener: OnItemSelectListener<Meeting>): RecyclerView.Adapter<MeetingListAdapter.MyViewHolder>() {

    private var meetings = ArrayList<Meeting>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(meetings: ArrayList<Meeting>) {
        this.meetings = meetings
        notifyDataSetChanged()
    }

    class MyViewHolder(view: ItemMeetingBinding) : RecyclerView.ViewHolder(view.root) {
        val binding: ItemMeetingBinding = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(ItemMeetingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val meeting = meetings[position]

        val binding = holder.binding

        binding.title.text = meeting.topic
        binding.duration.text = "${meeting.duration} mins"
        binding.time.text = meeting.startTime.toShowTime(meeting.timezone)

        binding.root.setOnClickListener { onItemSelectListener.onSelect(meeting) }

    }

    override fun getItemCount(): Int = meetings.size
}