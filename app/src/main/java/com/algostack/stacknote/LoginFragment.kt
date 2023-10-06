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
import com.algostack.stacknote.databinding.FragmentLoginBinding
import com.algostack.stacknote.model.UserResponse
import com.algostack.stacknote.model.userRequest
import com.algostack.stacknote.utils.NetworkResult
import com.algostack.stacknote.utils.TokenManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    var _binding : FragmentLoginBinding ?= null
    private val binding get() = _binding!!
    private val authViewModel by viewModels<AuthViewModel> (  )

    @Inject
    lateinit var tokenManager: TokenManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            val validateResutl = validateUserInpute()

            if(validateResutl.first){
                authViewModel.loginUser(getUserRequest())
            }
            else{
                binding.txtError.text = validateResutl.second
            }




        }

        binding.btnSignUp.setOnClickListener {

            findNavController().popBackStack()
        }

        bindOvservers()

    }

    fun getUserRequest() : userRequest {
        val emailAdress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()

        return userRequest(emailAdress,"",password)
    }

    fun validateUserInpute() : Pair<Boolean,String>{

        val userRequest = getUserRequest()
         return authViewModel.vlidateCredintials(userRequest.name, userRequest.email, userRequest.password, true)

    }

    private fun bindOvservers(){
        authViewModel.userReponseLiveData.observe(viewLifecycleOwner, Observer {

            binding.progressBar.isVisible = false
            when(it){
                is NetworkResult.Success -> {
                    tokenManager.saveToken(it.data!!.token)
                    findNavController().navigate(R.id.action_loginFragment2_to_mainFragment2)
                }
                is NetworkResult.Error -> {
                    binding.txtError.text = it.message
                }
                is NetworkResult.Loading -> {
                    binding.txtError.text = it.message
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }

}