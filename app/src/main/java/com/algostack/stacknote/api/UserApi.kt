package com.algostack.stacknote.api

import com.algostack.stacknote.model.UserResponse
import com.algostack.stacknote.model.userRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {

    @POST("/users")
   suspend fun signup(@Body userRequest: userRequest) : Response<UserResponse>

    @POST("/login")
    suspend fun signin(@Body userRequest: userRequest) : Response<UserResponse>
}