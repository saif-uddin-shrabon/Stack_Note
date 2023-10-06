package com.algostack.stacknote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.algostack.stacknote.databinding.FragmentRegistrationBinding
import com.algostack.stacknote.model.userRequest
import com.algostack.stacknote.utils.NetworkResult
import com.algostack.stacknote.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.time.Duration.Companion.days

@AndroidEntryPoint
class registrationFragment : Fragment() {

    private var _binding : FragmentRegistrationBinding? = null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel> (  )

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        if (tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_registrationFragment_to_mainFragment2)
        }


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignUp.setOnClickListener {

            val validationResult = vilidateUserInput()
            if(validationResult.first){
                authViewModel.registerUser(getUserRequest())

            }else{
                binding.txtError.text = validationResult.second
            }



        }

        binding.btnLogin.setOnClickListener {


            findNavController().navigate(R.id.action_registrationFragment_to_loginFragment2)
        }


        bindObservers()
    }

    private fun getUserRequest() : userRequest {
        val emailAdress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()
        return userRequest(emailAdress,userName,password)
    }

    private fun vilidateUserInput(): Pair<Boolean, String> {

          val userRequest = getUserRequest()
        return authViewModel.vlidateCredintials(userRequest.name,userRequest.email,userRequest.password, false)
    }

    private fun bindObservers() {
        authViewModel.userReponseLiveData.observe(viewLifecycleOwner, Observer {
            binding.progressBar.isVisible = false
            when (it) {
                is NetworkResult.Success -> {

                    //token
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_registrationFragment_to_mainFragment2)
                }

                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}