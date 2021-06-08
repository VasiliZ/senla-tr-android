package com.github.rtyvz.senla.tr.regexapp

import android.os.Bundle
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.github.rtyvz.senla.tr.regexapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val drawerMenuAdapter by lazy {
        MenuAdapter(layoutInflater, resources.getStringArray(R.array.drawer_item_list).toList())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.apply {
            toolbar.setNavigationIcon(R.drawable.drawable_menu_white)
            toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(Gravity.LEFT)
            }
            navigationItemList.adapter = drawerMenuAdapter
            navigationItemList.setOnItemClickListener { parent, view, position, id ->
                drawerMenuAdapter.setIndexForSelectedItem(position)
            }
        }
    }
}