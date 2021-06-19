package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.multiapp.ui.calc.CalcFragment
import com.github.rtyvz.senla.tr.multiapp.ui.main.adapter.DrawerSimpleAdapter
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.NotebookFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.RootNotebookFragment

class MainActivity : AppCompatActivity(), ChangeTitleToolBarContract {
    private lateinit var binding: ActivityMainBinding
    private var currentTag: String? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var adapterView: AdapterView<*>
    private var currentAdapterPosition: Int = -1

    companion object {
        const val KEY_FOR_ADAPTER_VALUE = "ADAPTER_VALUE"
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
        initDrawerToggle()

        binding.navigationItemList.apply {
            adapter = initAdapterSimple()
            setOnItemClickListener { parent, _, position, _ ->
                replaceFragmentByTag(parent, position)
                currentAdapterPosition = position
                (adapter as DrawerSimpleAdapter).setIndexForSelectedItem(position)
            }
        }

        if (currentTag == null) {
            replaceFragment(MainFragment())
        } else {
            replaceFragmentByTag(adapterView, currentAdapterPosition)
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.appBarMain.toolbar)
    }

    private fun initDrawerToggle() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            R.string.app_name,
            R.string.app_name
        )

        binding.drawerLayout
            .setDrawerListener(
                drawerToggle
            )
        drawerToggle.isDrawerIndicatorEnabled = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == android.R.id.home) {
            return true
        }

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        drawerToggle.onConfigurationChanged(newConfig)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        drawerToggle.syncState()
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

    private fun replaceFragmentByTag(parent: AdapterView<*>, position: Int) {
        adapterView = parent
        currentTag = (adapterView.adapter.getItem(position) as MutableMap<String, String>)[TAG]
        when (currentTag) {
            MainFragment.TAG -> replaceFragment(MainFragment())
            CalcFragment.TAG -> replaceFragment(CalcFragment())
            NotebookFragment.TAG -> replaceFragment(RootNotebookFragment())
            else -> {
                replaceFragment(MainFragment())
            }
        }
    }

    override fun changeToolbarBehavior(title: String?, isEditFragment: Boolean) {
        binding.appBarMain.toolbar.title = title
        if (isEditFragment) {
            binding.appBarMain.toolbar.setNavigationOnClickListener {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportFragmentManager.fragments[0].childFragmentManager.popBackStack()
            }
        } else {
            initDrawerToggle()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TAG, currentTag)
    }
}