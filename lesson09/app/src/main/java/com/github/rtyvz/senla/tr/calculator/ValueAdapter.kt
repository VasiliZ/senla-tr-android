package com.github.rtyvz.senla.tr.calculator

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.calculator.databinding.PreviousValueItemBinding

class ValueAdapter : RecyclerView.Adapter<ValueAdapter.ValueViewHolder>() {

    private val valueList: MutableList<String> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValueViewHolder {
        val view =
            PreviousValueItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ValueViewHolder(view)
    }

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

    fun removeItem() {
        notifyItemRemoved(0)
        valueList.removeAt(0)
    }

    fun insertItem(value: String) {
        notifyDataSetChanged()
        valueList.add(value)
    }

    fun insertItems(data: List<String>) {
        valueList.clear()
        valueList.addAll(data)
    }
}