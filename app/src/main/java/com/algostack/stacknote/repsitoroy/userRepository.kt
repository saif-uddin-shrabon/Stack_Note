package com.algostack.stacknote.repsitoroy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.stacknote.api.UserApi
import com.algostack.stacknote.model.UserResponse
import com.algostack.stacknote.model.userRequest
import com.algostack.stacknote.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class userRepository @Inject constructor (private val userApi: UserApi) {

    private val _userRespronseLiveData = MutableLiveData<NetworkResult<UserResponse>>()
    val userResponLiveData : LiveData<NetworkResult<UserResponse>>
        get() = _userRespronseLiveData


    suspend fun registerUser(userRequest: userRequest){

        _userRespronseLiveData.postValue(NetworkResult.Loading())

        val response = userApi.signup(userRequest)
        handleResponse(response)
    }


    suspend fun loginUser(userRequest: userRequest){
        _userRespronseLiveData.postValue(NetworkResult.Loading())

        val response = userApi.signin(userRequest)
        handleResponse(response)
    }

    private fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userRespronseLiveData.postValue(NetworkResult.Success(response.body()!!))

        } else if (response.errorBody() != null) {
            val errObj = JSONObject(response.errorBody()!!.charStream().readText())
            _userRespronseLiveData.postValue(NetworkResult.Error(errObj.getString("message")))

        } else {
            _userRespronseLiveData.postValue(NetworkResult.Error("Something went wrong"))
        }
    }




}