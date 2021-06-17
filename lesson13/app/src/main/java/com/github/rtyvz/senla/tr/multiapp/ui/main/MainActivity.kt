package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.multiapp.ext.bool
import com.github.rtyvz.senla.tr.multiapp.ui.calc.CalcFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.EditFileFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.NotebookFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.ParentFragmentNotebook
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.ResetDataFragmentContract

class MainActivity : AppCompatActivity(),
    ChangeTitleToolBarContract, ResetDataFragmentContract {
    private lateinit var binding: ActivityMainBinding
    private var currentTag: String? = null

    companion object {
        private const val ADAPTER_DATA_FIELD = "data"
        private const val TAG = "tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentTag = it.getString(TAG)
        }
        setSupportActionBar(binding.appBarMain.toolbar)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        binding.appBarMain.toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_menu_24)
            setNavigationOnClickListener {
                drawerLayout.openDrawer(GravityCompat.START)
            }
        }
        val simpleAdapter = SimpleAdapter(
            this,
            prepareDataFromAdapter(),
            R.layout.drawer_item,
            listOf(
                ADAPTER_DATA_FIELD,
                TAG
            ).toTypedArray(),
            intArrayOf(R.id.drawerItemTextView)
        )
        binding.navigationItemList.apply {
            adapter = simpleAdapter
            setOnItemClickListener { parent, _, position, _ ->
                parent.adapter.getItem(position)
                val dataAdapter = simpleAdapter.getItem(position) as LinkedHashMap<*, *>
                replaceFragmentByTag(dataAdapter[TAG].toString())
            }
        }

        if (currentTag == null) {
            replaceFragment(MainFragment())
        } else {
            replaceFragmentByTag(currentTag)
        }
    }

    private fun prepareDataFromAdapter(): ArrayList<Map<String, String>> {
        val listTags = listOf(MainFragment.TAG, NotebookFragment.TAG, CalcFragment.TAG)
        val listFromResource = resources.getStringArray(R.array.drawer_item_list)
        val data = ArrayList<Map<String, String>>(listFromResource.size)

        for (i in listTags.indices) {
            val map = mutableMapOf<String, String>()
            map[ADAPTER_DATA_FIELD] = listFromResource[i]
            map[TAG] = listTags[i]
            data.add(map)
        }

        return data
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

    override fun changeTitleToolBar(title: String?) {
        binding.appBarMain.toolbar.title = title
    }

    override fun changeToolbarNavIcon(drawable: Drawable?) {
        binding.appBarMain.toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TAG, currentTag)
    }

    override fun setContent(content: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
        if (fragment is EditFileFragment) {
            fragment.setPath(content)
        } else {
            replaceFragment(EditFileFragment().apply {
                arguments = Bundle().apply {
                    putString(EditFileFragment.EXTRA_FILE_PATH, content)
                }
            })
        }
    }

    override fun onCreateNewFileClicked() {
        if (R.bool.isLand.bool(this)) {
            val fragment = supportFragmentManager.findFragmentById(R.id.contentContainer)
            if (fragment is EditFileFragment) {
                fragment.setPath(null)
            }
        } else {
            replaceFragment(EditFileFragment())
        }
    }
}