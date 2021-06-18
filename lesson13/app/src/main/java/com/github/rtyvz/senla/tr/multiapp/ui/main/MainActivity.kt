package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.os.Bundle
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.multiapp.ui.calc.CalcFragment
import com.github.rtyvz.senla.tr.multiapp.ui.main.adapter.DrawerSimpleAdapter
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.NotebookFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.ParentNotebookFragment

class MainActivity : AppCompatActivity(),
    ChangeTitleToolBarContract {
    private lateinit var binding: ActivityMainBinding
    private var currentTag: String? = null

    companion object {
        const val KEY_FOR_ADAPTER_VALUE = "data"
        private const val TAG = "tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentTag = it.getString(TAG)
        }

        initToolBar()

        binding.navigationItemList.apply {
            adapter = initAdapterSimple()
            setOnItemClickListener { parent, _, position, _ ->
                setAdapterClickItemListener(position, parent)
                (adapter as DrawerSimpleAdapter).setIndexForSelectedItem(position)
            }
        }

        if (currentTag == null) {
            replaceFragment(MainFragment())
        } else {
            replaceFragmentByTag(currentTag)
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.apply {
            setNavigationIcon(R.drawable.ic_baseline_menu_24)
            openDrawer()
        }
    }

    private fun prepareDataFromAdapter(): ArrayList<Map<String, String>> {
        val listTags = listOf(MainFragment.TAG, NotebookFragment.TAG, CalcFragment.TAG)
        val listFromResource = resources.getStringArray(R.array.drawer_item_list)
        val data = ArrayList<Map<String, String>>(listFromResource.size)

        for (i in listTags.indices) {
            val map = mutableMapOf<String, String>()
            map[KEY_FOR_ADAPTER_VALUE] = listFromResource[i]
            map[TAG] = listTags[i]
            data.add(map)
        }

        return data
    }

    private fun initAdapterSimple() =
        DrawerSimpleAdapter(
            this,
            prepareDataFromAdapter(),
            R.layout.drawer_item,
            listOf(
                KEY_FOR_ADAPTER_VALUE,
                TAG
            ).toTypedArray(),
            intArrayOf(R.id.drawerItemTextView)
        )

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun setAdapterClickItemListener(
        position: Int,
        parent: AdapterView<*>
    ) {
        val dataAdapter = parent.adapter.getItem(position) as LinkedHashMap<*, *>
        replaceFragmentByTag(dataAdapter[TAG].toString())
    }

    private fun replaceFragmentByTag(tag: String?) {
        tag?.let {
            currentTag = tag
            when (tag) {
                MainFragment.TAG -> replaceFragment(MainFragment())
                CalcFragment.TAG -> replaceFragment(CalcFragment())
                NotebookFragment.TAG -> replaceFragment(
                    ParentNotebookFragment()
                )
            }
            binding.drawerLayout.closeDrawers()
        }
    }

    override fun changeToolbarBehavior(title: String?, isEditFragment: Boolean) {
        binding.appBarMain.toolbar.title = title
        if (isEditFragment) {
            binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24)
            binding.appBarMain.toolbar.setNavigationOnClickListener {
                supportFragmentManager.fragments[0].childFragmentManager.popBackStack()
            }
        } else {
            binding.appBarMain.toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24)
            openDrawer()
        }
    }

    private fun openDrawer() {
        binding.appBarMain.toolbar.setNavigationOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TAG, currentTag)
    }
}