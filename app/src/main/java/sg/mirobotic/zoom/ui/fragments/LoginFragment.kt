package sg.mirobotic.zoom.ui.fragments;

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import sg.mirobotic.zoom.MainViewModel
import sg.mirobotic.zoom.data.remote.ApiResponseListener
import sg.mirobotic.zoom.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private var safebinding: FragmentLoginBinding? = null
    private val binding get() = safebinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        safebinding = FragmentLoginBinding.inflate(inflater, container, false)
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
                binding.btnLogin.isEnabled = true
            }

        }

        binding.btnLogin.setOnClickListener {

            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                binding.etEmail.error = "Enter email"
                return@setOnClickListener
            }
            binding.etEmail.error = null

            val password = binding.etPassword.text.toString().trim()
            if (password.isEmpty()) {
                binding.etPassword.error = "Enter password"
                return@setOnClickListener
            }
            binding.etPassword.error = null


            binding.btnLogin.isEnabled = false
            mainViewModel.login(email, password, apiResponseListener)
        }

    }

}