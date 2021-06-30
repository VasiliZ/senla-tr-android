package com.github.rtyvz.senla.tr.dbapp.ui.comments

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.dbapp.databinding.CommentItemBinding
import com.github.rtyvz.senla.tr.dbapp.models.CommentWithEmailEntity

class CommentsAdapter(private val changeCommentRate: (String, Long, Long) -> Unit) :
    RecyclerView.Adapter<CommentsAdapter.CommentsViewHolder>() {
    private val listWithComments = mutableListOf<CommentWithEmailEntity>()

    class CommentsViewHolder(
        private val binding: CommentItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CommentWithEmailEntity) {
            binding.apply {
                emailTextView.text = data.email
                commentTextView.text = data.comment
                rateCommentTextView.text = data.rate.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentsViewHolder {
        val view = CommentItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CommentsViewHolder(
            view
        ).apply {
            view.incrRateButton.setOnClickListener {
                changeCommentRate(
                    "+1",
                    getItem(adapterPosition).commentId,
                    getItem(adapterPosition).postId
                )
            }
            view.decrRateButton.setOnClickListener {
                changeCommentRate(
                    "-1",
                    getItem(adapterPosition).commentId,
                    getItem(adapterPosition).postId
                )
            }
        }
    }

    private fun getItem(position: Int) = listWithComments[position]

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