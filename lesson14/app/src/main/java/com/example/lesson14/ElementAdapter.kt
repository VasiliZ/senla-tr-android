package com.example.lesson14

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.example.lesson14.databinding.ElementItemBinding
import com.example.lesson14.model.Element

class ElementAdapter(
        private val inflater: LayoutInflater,
        private val data: MutableList<Element>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = ElementItemBinding.inflate(inflater)
        val element = getItem(position)
        binding.apply {
            countTextView.text = element.countValue.toString()
            removeItemButton.setOnClickListener {
                data.remove(element)
                notifyDataSetChanged()
            }
            incButton.setOnClickListener {
                element.countValue = element.countValue.inc()
                countTextView.text = element.countValue.toString()
            }
            decButton.setOnClickListener {
                element.countValue = element.countValue.dec()
                countTextView.text = element.countValue.toString()
            }
        }

        return binding.root
    }

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = data.size

    fun addItem(element: Element) {
        data.add(element)
        notifyDataSetChanged()
    }

    fun getListWithData() = data.toList()
}