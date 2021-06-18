package com.github.rtyvz.senla.tr.multiapp.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SimpleAdapter
import android.widget.TextView
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.DrawerItemBinding
import com.github.rtyvz.senla.tr.multiapp.ui.main.MainActivity

class DrawerSimpleAdapter(
    private val context: Context,
    private val data: List<Map<String, String>>,
    resource: Int,
    from: Array<String>,
    destination: IntArray
) : SimpleAdapter(context, data, resource, from, destination) {
    private var selectedItemId = -1

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        val holder: ViewHolder
        var view: View? = null

        if (convertView?.tag == null) {
            val binding = DrawerItemBinding.inflate(LayoutInflater.from(context))
            view = binding.root
            holder = ViewHolder(binding)
        } else {
            holder = convertView.tag as ViewHolder
        }
        holder.itemTextView.text = data[position][MainActivity.KEY_FOR_ADAPTER_VALUE]

        if (selectedItemId == position) {
           holder.itemTextView.setTextColor(context.resources.getColor(R.color.teal_200, null))
        } else {
            holder.itemTextView.setTextColor(context.resources.getColor(R.color.black, null))
        }

        return view
    }

    inner class ViewHolder(binding: DrawerItemBinding) {
        val itemTextView: TextView = binding.drawerItemTextView
    }

    fun setIndexForSelectedItem(selectedItemId: Int) {
        this.selectedItemId = selectedItemId
        notifyDataSetChanged()
    }
}