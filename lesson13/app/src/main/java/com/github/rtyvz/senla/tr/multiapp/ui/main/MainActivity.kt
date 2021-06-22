package com.github.rtyvz.senla.tr.multiapp.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.MenuItem
import android.widget.SimpleAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.github.rtyvz.senla.tr.multiapp.R
import com.github.rtyvz.senla.tr.multiapp.databinding.ActivityMainBinding
import com.github.rtyvz.senla.tr.multiapp.ui.calc.CalcFragment
import com.github.rtyvz.senla.tr.multiapp.ui.main.adapter.DrawerSimpleAdapter
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.NotebookFragment
import com.github.rtyvz.senla.tr.multiapp.ui.nootebook.fragments.RootNotebookFragment

class MainActivity : AppCompatActivity(), CloseActivityContract {
    private lateinit var binding: ActivityMainBinding
    private var currentTag: String? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private var currentAdapterPosition: Int = -1
    private val simpleAdapter by lazy {
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
    }

    companion object {
        const val KEY_FOR_ADAPTER_VALUE = "ADAPTER_VALUE"
        private const val TAG = "tag"
        private const val EXTRA_ADAPTER_POSITION = "ADAPTER_POSITION"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        savedInstanceState?.let {
            currentTag = it.getString(TAG)
            currentAdapterPosition = it.getInt(EXTRA_ADAPTER_POSITION)
        }

        initToolBar()
        initDrawerListener()

        if (currentTag == null) {
            replaceFragment(MainFragment())
            simpleAdapter.setIndexForSelectedItem(0)
        } else {
            replaceFragmentByTag(simpleAdapter, currentAdapterPosition)
        }

        binding.navigationItemList.apply {
            adapter = simpleAdapter
            setOnItemClickListener { _, _, position, _ ->
                replaceFragmentByTag(simpleAdapter, position)
                currentAdapterPosition = position
                (adapter as DrawerSimpleAdapter).setIndexForSelectedItem(position)
            }
        }
    }

    private fun initToolBar() {
        setSupportActionBar(binding.appBarMain.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initDrawerListener() {
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.appBarMain.toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerToggle.syncState()
        binding.drawerLayout.addDrawerListener(drawerToggle)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true
        }

        return false
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

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainer, fragment, currentTag)
        transaction.addToBackStack(currentTag)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
    }

    private fun replaceFragmentByTag(adapter: SimpleAdapter, position: Int) {
        currentTag = (adapter.getItem(position) as MutableMap<String, String>)[TAG]
        onBackPressed()
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

        if (currentFragment != null && currentTag.equals(currentFragment.tag)) {
            return
        }

        when (currentTag) {
            MainFragment.TAG -> {
                replaceFragment(MainFragment())
                changeToolbarTitle(getString(R.string.app_name))
            }
            CalcFragment.TAG -> {
                replaceFragment(CalcFragment())
                changeToolbarTitle(getString(R.string.calc_fragment_title))
            }
            NotebookFragment.TAG -> {
                replaceFragment(RootNotebookFragment())
                changeToolbarTitle(getString(R.string.notebook_fragment_label))
            }
            else -> {
                replaceFragment(MainFragment())
                changeToolbarTitle(getString(R.string.app_name))
            }
        }
    }

    private fun changeToolbarTitle(title: String) {
        binding.appBarMain.toolbar.title = title
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerVisible(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString(TAG, currentTag)
        outState.putInt(EXTRA_ADAPTER_POSITION, currentAdapterPosition)
    }

    override fun finishActivity() {
        this.finish()
    }
}