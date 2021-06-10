package com.github.rtyvz.senla.tr.multiapp.ui

import android.os.Bundle
import android.view.Gravity
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.drawer.ui.nootebook.EditFileFragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.multiapp.ext.bool
import com.github.rtyvz.senla.tr.multiapp.ui.calc.CalcFragment
import com.github.rtyvz.senla.tr.multiapp.ui.main.MainFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.NotebookFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.ParentFragmentNotebook
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.ResetDataFragmentContract

class MainActivity : AppCompatActivity(),
    ChangeTitleToolBarContract, ResetDataFragmentContract {
    private lateinit var binding: ActivityMainBinding
    private var currentTag: String? = null

    companion object {
        const val ADAPTER_DATA_FIELD = "data"
        const val TAG_IDENTIFIER = "tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentTag = it.getString(TAG_IDENTIFIER)
        }
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
            map[ADAPTER_DATA_FIELD] = listFromResource[i]
            map[TAG_IDENTIFIER] = listTags[i]
            data.add(map)
        }

        val simpleAdapter = SimpleAdapter(
            this,
            data,
            R.layout.drawer_item,
            listOf(
                ADAPTER_DATA_FIELD,
                TAG_IDENTIFIER
            ).toTypedArray(),
            intArrayOf(R.id.drawerItemTextView)
        )

        binding.navigationItemList.adapter = simpleAdapter
        binding.navigationItemList.setOnItemClickListener { parent, _, position, _ ->
            parent.adapter.getItem(position)
            val dataAdapter = simpleAdapter.getItem(position) as LinkedHashMap<*, *>
            replaceFragmentByTag(dataAdapter[TAG_IDENTIFIER].toString())
        }
        currentTag?.let {
            replaceFragmentByTag(it)
        } ?: kotlin.run {
            replaceFragment(MainFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun replaceFragmentByTag(tag: String?) {
        tag?.let {
            currentTag = tag
            when (tag) {
                MainFragment.TAG -> replaceFragment(MainFragment())
                CalcFragment.TAG -> replaceFragment(CalcFragment())
                NotebookFragment.TAG -> {
                    if (R.bool.isLand.bool(this)) {
                        replaceFragment(ParentFragmentNotebook())
                    } else {
                        replaceFragment(NotebookFragment())
                    }
                }
            }
            binding.drawerLayout.closeDrawers()
        }
    }

    override fun changeToolBar(title: String?) {
        binding.appBarMain.toolbar.title = title
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TAG_IDENTIFIER, currentTag)
    }

    override fun setContent(content: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
        if (fragment is EditFileFragment) {
            fragment.setContent(content)
        } else {
            replaceFragment(EditFileFragment().apply {
                arguments = Bundle().apply {
                    putString(EditFileFragment.PATH_FILE_EXTRA, content)
                }
            })
        }
    }

    override fun openNewFile() {
        if (R.bool.isLand.bool(this)) {
            val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditFileFragment) {
                fragment.setContent(null)
            }
        } else {
            replaceFragment(EditFileFragment())
        }
    }
}