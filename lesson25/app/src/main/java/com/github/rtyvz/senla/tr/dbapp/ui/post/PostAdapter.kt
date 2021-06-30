package com.github.rtyvz.senla.tr.dbapp.ui.post

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.dbapp.databinding.PostItemBinding
import com.github.rtyvz.senla.tr.dbapp.models.PostAndUserEmailEntity

class PostAdapter(private val clickAction: (Long) -> (Unit)) :
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
        val view = PostItemBinding.inflate(LayoutInflater
            .from(parent.context), parent, false)

        return PostViewHolder(view).apply {
            view.root.setOnClickListener {
                clickAction(getItem(adapterPosition).postId)
            }
        }
    }

    private fun getItem(position: Int): PostAndUserEmailEntity {
        return postDatumEmails[position]
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