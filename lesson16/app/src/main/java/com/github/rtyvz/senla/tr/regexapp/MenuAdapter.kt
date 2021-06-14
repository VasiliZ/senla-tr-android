package com.github.rtyvz.senla.tr.regexapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.github.rtyvz.senla.tr.regexapp.databinding.DrawerItemBinding
import com.github.rtyvz.senla.tr.regexapp.entity.MenuItem

private fun Int.color(context: Context) = context.resources.getColor(this, null)

class MenuAdapter(
    private val inflater: LayoutInflater,
    private val data: List<MenuItem>
) : BaseAdapter() {
    private var selectedItem = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: MenuViewHolder?

        if (convertView?.tag == null) {
            val binding = DrawerItemBinding.inflate(inflater)
            view = binding.root
            viewHolder = MenuViewHolder(binding)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = convertView.tag as MenuViewHolder
        }

        val item = getItem(position)
        viewHolder.apply {
            itemTextView.text = item.title
            itemIcon.setImageDrawable(item.icon)
        }
        changeColorMenuItem(position, view)

        return view
    }

    private fun changeColorMenuItem(position: Int, view: View) {
        if (selectedItem == position) {
            view.setBackgroundColor(R.color.purple_200.color(inflater.context))
        } else {
            view.setBackgroundColor(R.color.white.color(inflater.context))
        }
    }

    override fun getItem(position: Int) = data[position]

    override fun getItemId(position: Int) = position.toLong()

    override fun getCount() = data.size

    fun setIndexForSelectedItem(selectedPosition: Int) {
        selectedItem = selectedPosition
        notifyDataSetChanged()
    }

    private class MenuViewHolder(binding: DrawerItemBinding) {
        val itemTextView = binding.drawerItemTextView
        val itemIcon = binding.drawerMenuIcon
    }
}