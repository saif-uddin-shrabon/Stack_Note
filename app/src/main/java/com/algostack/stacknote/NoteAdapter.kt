package com.algostack.stacknote

import android.view.LayoutInflater
import android.view.View
import android.widget.ListAdapter
import com.algostack.stacknote.model.NoteResponse
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.algostack.stacknote.databinding.NoteItemBinding
import com.algostack.stacknote.model.DataX
import kotlin.reflect.KFunction1

class NoteAdapter(private val onNoteClicked: (DataX) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<DataX, NoteAdapter.NoteViewHolder>(ComparatorDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val binding = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = getItem(position)
        holder.bind(note)
    }




    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: DataX) {

            binding.title.text = note.title
            binding.desc.text = note.Description

            binding.root.setOnClickListener {
                onNoteClicked(note)
            }
        }

    }






    class ComparatorDiffUtil : DiffUtil.ItemCallback<DataX>() {
        override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {

            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
            return oldItem == newItem
        }
    }
}
