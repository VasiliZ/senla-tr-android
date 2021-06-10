package com.github.rtyvz.senla.tr.regexapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.rtyvz.senla.tr.regexapp.databinding.DrawerItemBinding
import com.github.rtyvz.senla.tr.regexapp.entity.MenuItem

class MenuAdapter(
    private val inflater: LayoutInflater,
    private val data: List<MenuItem>
) : BaseAdapter() {
    private var selectedItem = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding = DrawerItemBinding.inflate(inflater)
        val item = getItem(position)
        binding.drawerItemTextView.text = item.title
        binding.drawerMenuIcon.setImageDrawable(item.icon)

        if (selectedItem == position) {
            binding.root.setBackgroundColor(
                inflater.context.resources.getColor(
                    R.color.purple_200,
                    null
                )
            )
        }

        return binding.root
    }

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = data.size

    fun setIndexForSelectedItem(position: Int) {
        selectedItem = position
        notifyDataSetChanged()
    }
}