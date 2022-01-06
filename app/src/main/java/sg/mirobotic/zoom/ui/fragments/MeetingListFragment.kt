package sg.mirobotic.zoom.ui.fragments;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import sg.mirobotic.zoom.MainViewModel
import sg.mirobotic.zoom.data.Meeting
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.databinding.FragmentLoginBinding
import sg.mirobotic.zoom.databinding.FragmentMeetingListBinding
import sg.mirobotic.zoom.ui.adapters.MeetingListAdapter
import sg.mirobotic.zoom.ui.adapters.OnItemSelectListener
import sg.mirobotic.zoom.utils.addDivider

class MeetingListFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var safebinding: FragmentMeetingListBinding? = null
    private val binding get() = safebinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        safebinding = FragmentMeetingListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiResponseListener = object : ApiResponseListener<String> {
            override fun onSuccess(msg: String) {
                Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT).show()
                activity?.onBackPressed()
            }

            override fun onError(msg: String) {
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
            }

        }

        val adapter = MeetingListAdapter(object : OnItemSelectListener<Meeting> {
            override fun onSelect(item: Meeting) {
                val action = MeetingListFragmentDirections.actionMeetingListFragmentToJoinMeetingFragment(item)
                findNavController().navigate(action)
            }
        })

        binding.list.addDivider()
        binding.list.adapter = adapter

        mainViewModel.mMeetings.observe(viewLifecycleOwner, {
            adapter.setData(it)
        })

        mainViewModel.getMeetings(apiResponseListener)

    }

}