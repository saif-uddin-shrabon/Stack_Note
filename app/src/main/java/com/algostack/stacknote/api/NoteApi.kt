package com.algostack.stacknote.api

import com.algostack.stacknote.model.DataXX
import com.algostack.stacknote.model.DeleteResponse
import com.algostack.stacknote.model.NoteRequest
import com.algostack.stacknote.model.NoteResponse
import com.algostack.stacknote.model.UpdateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {

    @GET("/getAllNotePost")
    suspend fun getNote(): Response<NoteResponse>

    @POST("/createNotePost")
    suspend fun createNote(@Body noteRequest: NoteRequest) : Response<DeleteResponse>

    @PUT("/updateNotePost/{noteid}")
    suspend fun updateNote(
        @Path("noteid") noteId: String,
        @Body noteRequest: NoteRequest
    ): Response<UpdateResponse>



    @DELETE("/deleteNotePost/{deleteid}")
    suspend fun deleteNote(@Path("deleteid") id: String) : Response<DeleteResponse>



}