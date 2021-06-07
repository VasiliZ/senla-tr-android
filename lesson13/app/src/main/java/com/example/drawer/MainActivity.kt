package com.example.drawer

import android.os.Bundle
import android.view.Gravity
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.drawer.databinding.ActivityMainBinding
import com.example.drawer.ui.calc.CalcFragment
import com.example.drawer.ui.main.MainFragment
import com.example.drawer.ui.nootebook.NotebookFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val listTags = listOf(MainFragment.TAG, NotebookFragment.TAG, CalcFragment.TAG)
        binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
        binding.appBarMain.toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)
        }
        val listFromResource = resources.getStringArray(R.array.drawer_item_list)
        val data = ArrayList<Map<String, String>>(listFromResource.size)

        for (i in listTags.indices) {
            val map = mutableMapOf<String, String>()
            map["data"] = listFromResource[i]
            map["tag"] = listTags[i]
            data.add(map)
        }

        val simpleAdapter = SimpleAdapter(
            this,
            data,
            R.layout.drawer_item,
            listOf("data", "tag").toTypedArray(),
            intArrayOf(R.id.drawerItemTextView)
        )

        binding.navigationItemList.adapter = simpleAdapter
        binding.navigationItemList.setOnItemClickListener { parent, view, position, id ->
            parent.adapter.getItem(position)
            val dataAdapter = simpleAdapter.getItem(position) as LinkedHashMap<*, *>
            replaceFragmentByTag(dataAdapter["tag"].toString())
        }
        replaceFragment(MainFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.addToBackStack(null)
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

    private fun replaceFragmentByTag(tag: String?) {
        tag?.let {
            when (tag) {
                MainFragment.TAG -> replaceFragment(MainFragment())
                CalcFragment.TAG -> replaceFragment(CalcFragment())
                NotebookFragment.TAG -> replaceFragment(NotebookFragment())
            }
            binding.drawerLayout.close()
        }
    }
}