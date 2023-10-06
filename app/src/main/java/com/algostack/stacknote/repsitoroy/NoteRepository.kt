package com.algostack.stacknote.repsitoroy

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.algostack.stacknote.api.NoteApi
import com.algostack.stacknote.model.DataX
import com.algostack.stacknote.model.DataXX
import com.algostack.stacknote.model.DeleteResponse
import com.algostack.stacknote.model.NoteRequest
import com.algostack.stacknote.model.NoteResponse
import com.algostack.stacknote.model.UpdateResponse
import com.algostack.stacknote.utils.Constants
import com.algostack.stacknote.utils.NetworkResult
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class NoteRepository @Inject constructor (private val noteApi: NoteApi){

    private val _noteLiveData = MutableLiveData<NetworkResult<NoteResponse>>()
    val notesLiveData get() = _noteLiveData


    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    suspend fun getNotes(){
        _noteLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.getNote()
         Log.d("TAGNoteResponse", response.body().toString())



        if (response.isSuccessful && response.body() != null) {
            val noteList = listOf(response.body()!!)
            println("chekNote2: $noteList")
            _noteLiveData.postValue(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _noteLiveData.postValue(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _noteLiveData.postValue(NetworkResult.Error("Something Went Wrong"))
        }

    }


    suspend fun createNote(noteRequest: NoteRequest) {
        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.createNote(noteRequest)
        handleDeleteResponse(response, "Note Created")
    }



    suspend fun deleteNode(noteId: String){

        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.deleteNote(noteId)
        handleDeleteResponse(response,"Note Deleted")


    }

    private fun handleDeleteResponse(response: Response<DeleteResponse>, s: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(Pair(true, s)))
        } else {
            _statusLiveData.postValue(NetworkResult.Success(Pair(false, "Something went wrong")))
        }

    }

    suspend fun updateNote(notId: String, noteRequest: NoteRequest){


        _statusLiveData.postValue(NetworkResult.Loading())
        val response = noteApi.updateNote(notId,noteRequest)
        handlenewResponse(response,"Note Updated")

    }

    private fun handlenewResponse(response: Response<UpdateResponse>, s: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(Pair(true, s)))
        } else {
            _statusLiveData.postValue(NetworkResult.Success(Pair(false, "Something went wrong")))
        }

    }

    private fun handleResponse(response: Response<NoteResponse>, message: String) {
        if (response.isSuccessful && response.body() != null) {
            _statusLiveData.postValue(NetworkResult.Success(Pair(true, message)))
        } else {
            _statusLiveData.postValue(NetworkResult.Success(Pair(false, "Something went wrong")))
        }
    }

}




