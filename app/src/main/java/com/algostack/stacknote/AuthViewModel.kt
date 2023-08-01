package com.algostack.stacknote

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.algostack.stacknote.model.UserResponse
import com.algostack.stacknote.model.userRequest
import com.algostack.stacknote.repsitoroy.userRepository
import com.algostack.stacknote.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val userRepository: userRepository): ViewModel(){

    val userReponseLiveData : LiveData<NetworkResult<UserResponse>>
        get() = userRepository.userResponLiveData


    fun registerUser(userRequest: userRequest){
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }

    }

    fun loginUser(userRequest: userRequest){

        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

    fun vlidateCredintials(username: String, emailAddress: String, password: String, isLogin: Boolean) : Pair<Boolean, String> {

        var result = Pair(true, "")
        if ((!isLogin && TextUtils.isEmpty(username)) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)) {
               result = Pair(false, "Please provie the credentials")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            result = Pair(false, "Please provie valid email")
        }
        else if (password.length <=5 ){
            result = Pair(false, "Please length should be greater then 5")
        }

        return result
    }




}