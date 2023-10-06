package com.algostack.stacknote

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.algostack.stacknote.databinding.FragmentMainBinding
import com.algostack.stacknote.model.DataX
import com.algostack.stacknote.model.NoteResponse
import com.algostack.stacknote.utils.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class mainFragment : Fragment() {
    private var _binding : FragmentMainBinding?= null
    private  val binding get() =  _binding!!

    private val noteViewModel by viewModels<NoteViewModel> ()

    private lateinit var adapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = NoteAdapter(this::onNoteClicked)

        noteViewModel.getNotes()
        binding.noteList.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.noteList.adapter = adapter
        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_noteFragment)
        }
        bindObservers()
    }

    private fun bindObservers() {
        noteViewModel.notesLiveData.observe(viewLifecycleOwner, Observer { result ->
            binding.progressBar.isVisible = false
            when (result) {
                is NetworkResult.Success -> {
                    val dataXList = result.data?.data
                    adapter.submitList(dataXList)
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), result.message.toString(), Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        })
    }

    private fun onNoteClicked(dataX: DataX) {
        val bundle = Bundle()
        bundle.putString("note", Gson().toJson(dataX))
        findNavController().navigate(R.id.action_mainFragment_to_noteFragment, bundle)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}