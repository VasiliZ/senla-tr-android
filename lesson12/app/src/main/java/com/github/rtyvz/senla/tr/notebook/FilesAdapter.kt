package com.github.rtyvz.senla.tr.notebook

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
    private val dateFormatter = SimpleDateFormat(DATE_FORMAT, Locale.getDefault())

    companion object {
        private const val DATE_FORMAT = "dd-MM-yyyy"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilesViewHolder {
        val binding = FileDetailsItemBinding
            .inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return FilesViewHolder(binding).apply {
            view.root.setOnClickListener {
                click(getItem(adapterPosition).absolutePath)
            }
        }
    }

    override fun onBindViewHolder(holder: FilesViewHolder, position: Int) {
        holder.bind(getItem(position), dateFormatter)
    }

    class FilesViewHolder(val view: FileDetailsItemBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bind(
            item: File,
            dateFormatter: SimpleDateFormat
        ) {
            view.nameFileTextView.text = item.name
            view.lastEditTextView.text = dateFormatter
                .format(Date(item.lastModified()))
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<File>() {
        override fun areItemsTheSame(oldItem: File, newItem: File) = oldItem == newItem

        override fun areContentsTheSame(oldItem: File, newItem: File) = oldItem == newItem
    }
}

