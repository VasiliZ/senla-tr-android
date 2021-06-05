package com.github.rtyvz.senla.tr.notebook.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.notebook.databinding.FileDetailsItemBinding
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FilesAdapter(private val click: (String) -> Unit) :
    ListAdapter<File, FilesAdapter.FilesViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        val binding =
            FileDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        holder.bind(getItem(position), click)
    }

    class DiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem == newItem

        override fun areContentsTheSame(oldItem: File, newItem: File) = oldItem == newItem
    }

    class FilesViewHolder(private val view: FileDetailsItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        companion object {
            private const val DATE_FORMAT = "dd-MM-yyyy"
        }

        fun bind(item: File, click: (String) -> Unit) {
            val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())
            view.nameFileTextView.text = item.name
            view.lastEditTextView.text = dateFormatter.format(Date(item.lastModified()))
            view.root.setOnClickListener {
                click(item.absolutePath)
            }
        }
    }
}

