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
import sg.mirobotic.zoom.R
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var safebinding: FragmentHomeBinding? = null
    private val binding get() = safebinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        safebinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }

        binding.btnCreateMeeting.setOnClickListener {
            findNavController().navigate(R.id.createMeetingFragment)
        }

        binding.btnMeetings.setOnClickListener {
            findNavController().navigate(R.id.meetingListFragment)
        }

        val apiResponseListener = object : ApiResponseListener<String> {
            override fun onSuccess(msg: String) {
                Toast.makeText(requireContext(), "Profile success", Toast.LENGTH_SHORT).show()
            }

            override fun onError(msg: String) {
                Toast.makeText(requireContext(), "Profile failed", Toast.LENGTH_SHORT).show()
            }

        }

        mainViewModel.initZoom.observe(viewLifecycleOwner, {
            if (it) {
                binding.homeView.visibility = View.VISIBLE
                binding.initView.visibility = View.GONE

                binding.btnLogin.visibility = View.GONE
                binding.btnCreateMeeting.visibility = View.VISIBLE
                binding.btnMeetings.visibility = View.VISIBLE
            }else {
                binding.homeView.visibility = View.GONE
                binding.initView.visibility = View.VISIBLE

                binding.btnLogin.visibility = View.VISIBLE
                binding.btnCreateMeeting.visibility = View.GONE
                binding.btnMeetings.visibility = View.GONE
            }
        })

        mainViewModel.loginResult.observe(viewLifecycleOwner, {
            if (it) {
                mainViewModel.setListener()
                mainViewModel.getProfile(apiResponseListener)
                binding.btnLogin.visibility = View.GONE
                binding.btnCreateMeeting.visibility = View.VISIBLE
                binding.btnMeetings.visibility = View.VISIBLE
                Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT).show()
            }else {
                binding.btnLogin.visibility = View.VISIBLE
                binding.btnCreateMeeting.visibility = View.GONE
                binding.btnMeetings.visibility = View.GONE
                Toast.makeText(requireContext(), "Login failed", Toast.LENGTH_SHORT).show()
                binding.btnLogin.isEnabled = true
            }
        })
    }

}