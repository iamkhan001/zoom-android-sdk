package sg.mirobotic.zoom.ui.fragments;

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import sg.mirobotic.zoom.MainViewModel
import sg.mirobotic.zoom.R
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.data.MeetingDetails
import sg.mirobotic.zoom.data.MeetingTypes
import sg.mirobotic.zoom.data.Settings
import sg.mirobotic.zoom.databinding.FragmentCreateMeetingBinding
import sg.mirobotic.zoom.utils.MyMessage
import sg.mirobotic.zoom.utils.toServerDateFormat
import sg.mirobotic.zoom.utils.toServerTimeFormat
import java.util.*

class CreateMeetingFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var safebinding: FragmentCreateMeetingBinding? = null
    private val binding get() = safebinding!!

    private var date = ""
    private var time = ""
    private var type = MeetingTypes.MEETING_TYPE_SCHEDULE

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        safebinding = FragmentCreateMeetingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiResponseListener = object : ApiResponseListener<MeetingDetails> {

            override fun onData(data: MeetingDetails) {
                super.onData(data)
                mainViewModel.saveMeeting(data)
                if (type == MeetingTypes.MEETING_TYPE_INSTANT) {
                    mainViewModel.joinMeeting(requireContext(), data)
                    activity?.onBackPressed()
                }
            }

            override fun onSuccess(msg: String) {
                MyMessage.showToast(requireContext(), "Meeting Created")
                if (type != MeetingTypes.MEETING_TYPE_INSTANT) {
                    activity?.onBackPressed()
                }
            }

            override fun onError(msg: String) {
                MyMessage.showToast(requireContext(), "Error Unable to create meeting")
            }
        }

        binding.rgMeetingType.setOnCheckedChangeListener { _, id ->
            if (id == R.id.rbInstant) {
                date = Date().toServerDateFormat()
                time = Date().toServerTimeFormat()
                binding.titleDate.visibility = View.GONE
                binding.date.visibility = View.GONE
                binding.titleTile.visibility = View.GONE
                binding.time.visibility = View.GONE
                binding.btnCreate.text = "Start Meeting"
                type = MeetingTypes.MEETING_TYPE_INSTANT
            }else {
                binding.titleDate.visibility = View.VISIBLE
                binding.date.visibility = View.VISIBLE
                binding.titleTile.visibility = View.VISIBLE
                binding.time.visibility = View.VISIBLE
                if (date.isNotEmpty()) {
                    binding.date.text = date
                }
                if (time.isNotEmpty()) {
                    binding.time.text = time
                }
                type = MeetingTypes.MEETING_TYPE_SCHEDULE
                binding.btnCreate.text = "Create Meeting"
            }
        }


        binding.btnCreate.setOnClickListener {

            val title = binding.etTitle.text.toString().trim()
            if (title.isEmpty()) {
                binding.etTitle.error = "Enter meeting title"
                return@setOnClickListener
            }
            binding.etTitle.error = null

            val agenda = binding.etAgenda.text.toString().trim()
            if (agenda.isEmpty()) {
                binding.etAgenda.error = "Enter meeting agenda"
                return@setOnClickListener
            }
            binding.etAgenda.error = null

            val password = binding.etPassword.text.toString().trim()
            if (password.isEmpty()) {
                binding.etPassword.error = "Enter password"
                return@setOnClickListener
            }
            binding.etPassword.error = null

            if (date.isEmpty()) {
                return@setOnClickListener
            }

            if (time.isEmpty()) {
                return@setOnClickListener
            }

            val duration = binding.etDuration.text.toString().trim()
            if (duration.isEmpty()) {
                binding.etDuration.error = "Enter duration"
                return@setOnClickListener
            }
            binding.etDuration.error = null

            val meeting = MeetingDetails(
                title,
                type,
                "${date}T${time}",
                duration,
                mainViewModel.email.value!!,
                TimeZone.getDefault().displayName,
                password,
                agenda,
                Settings.getDefaultSettings()
            )

            mainViewModel.createMeetings(meeting, apiResponseListener)
        }

        val cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            date = cal.time.toServerDateFormat()
            binding.date.text = date
        }

        binding.date.setOnClickListener {
            DatePickerDialog(requireContext(),
                dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.time.setOnClickListener {
            val curTime = Calendar.getInstance()
            val mTimePicker = TimePickerDialog(requireContext(),
                { _, hourOfDay, minuteOfHour ->
                    time = String.format("%d:%d", hourOfDay, minuteOfHour)
                    binding.time.text = time
                },  curTime.get(Calendar.HOUR_OF_DAY), curTime.get(Calendar.MINUTE), false)
            mTimePicker.show()
        }

    }

}