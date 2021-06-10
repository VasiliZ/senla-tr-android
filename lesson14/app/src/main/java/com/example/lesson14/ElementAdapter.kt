package com.example.lesson14

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.core.widget.addTextChangedListener
import com.example.lesson14.databinding.ElementItemBinding
import com.example.lesson14.model.Element

class ElementAdapter(
    private val inflater: LayoutInflater,
    private val data: MutableList<Element>
) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView?.tag == null) {
            val binding = ElementItemBinding.inflate(inflater)
            view = binding.root
            viewHolder = ViewHolder(binding)
            convertView?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as ViewHolder
        }

        val element = getItem(position)
        viewHolder.apply {
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

        return view
    }

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = data.size

    fun addItem(element: Element) {
        data.add(element)
        notifyDataSetChanged()
    }

    fun getListWithData() = data.toList()

    class ViewHolder(binding: ElementItemBinding) {
        val countTextView = binding.countTextView
        val decButton = binding.decButton
        val incButton = binding.incButton
        val removeItemButton = binding.removeItemButton
    }
}