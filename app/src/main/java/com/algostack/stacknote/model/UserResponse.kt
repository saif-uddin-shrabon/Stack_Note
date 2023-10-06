package com.algostack.stacknote.model

data class UserResponse(
//    val `data`: List<Data>,
    val status: Int,
    val `data` : Data,
    val token: String
)