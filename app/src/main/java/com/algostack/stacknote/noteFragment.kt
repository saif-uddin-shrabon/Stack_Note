package com.algostack.stacknote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.algostack.stacknote.databinding.FragmentNoteBinding
import com.algostack.stacknote.model.DataX
import com.algostack.stacknote.model.DataXX
import com.algostack.stacknote.model.NoteRequest
import com.algostack.stacknote.model.NoteResponse
import com.algostack.stacknote.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class noteFragment : Fragment() {

  private  var _binding: FragmentNoteBinding? = null
    private  val binding get() = _binding!!

    private var note: DataX? = null
    private val noteViewModel by viewModels<NoteViewModel> ()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        binding.btnDelete.setOnClickListener {
            note?.let { noteViewModel.deleteNote(it!!._id) }
        }

        binding.btnSubmit.setOnClickListener {
            val title = binding.txtTitle.text.toString()
            val description = binding.txtDescription.text.toString()
            val noteRequest = NoteRequest(DataXX(title,description))
            if (note == null){
                noteViewModel.createNote(noteRequest)
            }else{
                println("CheckNoteID: ${note!!._id}")
                noteViewModel.updateNote(note!!._id,noteRequest)

            }
        }

    }

    private fun bindHandlers() {
        noteViewModel.statusLiveData.observe(viewLifecycleOwner, Observer{
            when(it){
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
                }
                is NetworkResult.Error -> {

                }
                is NetworkResult.Loading -> {

                }
            }
        })

    }

    private fun setInitialData() {
      val jsonNote = arguments?.getString("note")
        if(jsonNote != null){
          note =Gson().fromJson(jsonNote, DataX::class.java)

            note?.let {
                binding.txtTitle.setText(it.title)
                binding.txtDescription.setText(it.Description)
            }
        }else{
            binding.addEditText.text = "Add Note"
        }
    }


}