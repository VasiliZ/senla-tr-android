package com.github.rtyvz.senla.tr.dbapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.dbapp.databinding.PostItemBinding
import com.github.rtyvz.senla.tr.dbapp.models.PostAndUserEmailEntity

class PostAdapter :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    private var postDatumEmails: MutableList<PostAndUserEmailEntity> = mutableListOf()

    class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(postDataEmail: PostAndUserEmailEntity) {
            binding.postTitleTextView.text = postDataEmail.titlePost
            binding.authorEmailTextView.text = postDataEmail.userEmail
            binding.postBodyTextView.text = postDataEmail.postBody
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder(PostItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun getItemCount() = postDatumEmails.size

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(postDatumEmails[position])
    }

    fun setData(newData: List<PostAndUserEmailEntity>) {
        postDatumEmails.clear()
        postDatumEmails.addAll(newData)
        notifyDataSetChanged()
    }
}