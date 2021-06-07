package com.github.rtyvz.senla.tr.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.calculator.databinding.PreviousValueItemBinding

class ValueAdapter : RecyclerView.Adapter<ValueAdapter.ValueViewHolder>() {
    private val valueList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ValueViewHolder(
        PreviousValueItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ValueViewHolder, position: Int) {
        holder.bind(valueList[position])
    }

    class ValueViewHolder(private val view: PreviousValueItemBinding) :
        RecyclerView.ViewHolder(view.root) {
        fun bind(value: String) {
            view.data.text = value
        }
    }

    override fun getItemCount() = valueList.size

    fun removeItem(position: Int) {
        valueList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun insertItem(value: String) {
        valueList.add(value)
        notifyDataSetChanged()
    }

    fun insertItems(data: List<String>) {
        valueList.clear()
        valueList.addAll(data)
    }
}