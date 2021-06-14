package com.github.rtyvz.senla.tr.regexapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import com.github.rtyvz.senla.tr.regexapp.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.regexapp.entity.MenuItem

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val drawerMenuAdapter by lazy {
        MenuAdapter(layoutInflater, populateMenuList())
    }

    companion object {
        private const val START_INDEX = 0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.apply {
            toolbar.setNavigationIcon(R.drawable.drawable_menu_white)
            toolbar.setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
            drawerMenuAdapter.setIndexForSelectedItem(START_INDEX)
            navigationItemList.apply {
                adapter = drawerMenuAdapter
                navigationItemList.setOnItemClickListener { _, _, position, _ ->
                    drawerMenuAdapter.setIndexForSelectedItem(position)
                    replaceFragment(position)
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            replaceFragment(START_INDEX)
        }
    }

    private fun populateMenuList(): List<MenuItem> {
        return resources.getStringArray(R.array.drawer_item_list).mapIndexed { index, value ->
            when (index) {
                1 -> MenuItem(
                    ResourcesCompat.getDrawable(
                        resources,
                        R.drawable.drawable_settings_black,
                        null
                    ), value
                )
                else -> {
                    MenuItem(
                        ResourcesCompat.getDrawable(
                            resources,
                            R.drawable.drawable_home_black,
                            null
                        ), value
                    )
                }
            }
        }
    }

    private fun replaceFragment(position: Int) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        when (position) {
            0 -> transaction.replace(R.id.fragmentContainer, MainFragment())
            1 -> transaction.replace(R.id.fragmentContainer, SettingsFragment())
        }
        transaction.addToBackStack(null)
        transaction.commit()
    }
}