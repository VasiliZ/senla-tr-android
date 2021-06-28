package com.github.rtyvz.senla.tr.dbapp.ui.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.dbapp.databinding.CommentItemBinding
import com.github.rtyvz.senla.tr.dbapp.models.CommentWithEmailEntity

class CommentsAdapter : RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    private val listWithComments = mutableListOf<CommentWithEmailEntity>()

    class CommentsViewHolder(private val binding: CommentItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommentWithEmailEntity) {
            binding.apply {
                emailTextView.text = data.email
                commentTextView.text = data.comment
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        return CommentsViewHolder(
            CommentItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = listWithComments.size

    override fun onBindViewHolder(holder: CommentsViewHolder, position: Int) {
        holder.bind(listWithComments[position])
    }

    fun setData(listData: List<CommentWithEmailEntity>) {
        listWithComments.clear()
        listWithComments.addAll(listData)
        notifyDataSetChanged()
    }
}