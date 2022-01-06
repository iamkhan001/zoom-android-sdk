package sg.mirobotic.zoom.ui.fragments;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import sg.mirobotic.zoom.MainViewModel
import sg.mirobotic.zoom.data.Meeting
import sg.mirobotic.zoom.data.MeetingDetails
import sg.mirobotic.zoom.data.TextInfo
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.databinding.FragmentJoinMeetingBinding
import sg.mirobotic.zoom.ui.adapters.TextInfoAdapter
import sg.mirobotic.zoom.utils.addDivider

class JoinMeetingFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var safebinding: FragmentJoinMeetingBinding? = null
    private val binding get() = safebinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        safebinding = FragmentJoinMeetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val meeting = requireArguments().getSerializable("meeting") as Meeting? ?: return

        val apiResponseListener = object : ApiResponseListener<MeetingDetails> {

            override fun onData(data: MeetingDetails) {
                super.onData(data)
                setMeetingDetails(data)
            }

            override fun onSuccess(msg: String) {
                Toast.makeText(requireContext(), "success", Toast.LENGTH_SHORT).show()
            }

            override fun onError(msg: String) {
                Toast.makeText(requireContext(), "error: $msg", Toast.LENGTH_SHORT).show()
            }

        }

        mainViewModel.getMeetingDetails(meeting.id, apiResponseListener)


    }

    private fun setMeetingDetails(data: MeetingDetails) {

        val list = ArrayList<TextInfo>()
        list.add(TextInfo("Meeting Id", data.id.toString()))
        list.add(TextInfo("Topic", data.topic))
        list.add(TextInfo("Agenda", data.agenda))
        list.add(TextInfo("Host Email", data.hostEmail))
        list.add(TextInfo("Start Time", data.startTime))
        list.add(TextInfo("Timezone", data.timezone))
        list.add(TextInfo("Status", data.status))
        list.add(TextInfo("Start URL", data.startUrl))
        list.add(TextInfo("Join URL", data.joinUrl))

        binding.list.addDivider()

        binding.list.adapter = TextInfoAdapter(list)

        binding.btnJoin.visibility = View.VISIBLE

        binding.btnJoin.setOnClickListener {

            mainViewModel.joinMeeting(requireContext(), data)

        }

    }

}