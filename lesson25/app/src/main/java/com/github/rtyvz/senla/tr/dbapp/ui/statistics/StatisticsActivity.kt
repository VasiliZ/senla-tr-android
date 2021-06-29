package com.github.rtyvz.senla.tr.dbapp.ui.statistics

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import com.github.rtyvz.senla.tr.dbapp.R
import com.github.rtyvz.senla.tr.dbapp.databinding.StatisticsActivityBinding
import com.github.rtyvz.senla.tr.dbapp.models.StatisticsEntity

class StatisticsActivity : AppCompatActivity(),
    LoaderManager.LoaderCallbacks<List<StatisticsEntity>> {
    private lateinit var binding: StatisticsActivityBinding
    private val statisticsAdapter by lazy {
        StatisticsAdapter()
    }
    private lateinit var loader: Loader<List<StatisticsEntity>>

    companion object {
        private const val LOADER_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = StatisticsActivityBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.statisticsList.adapter = statisticsAdapter
        loader = supportLoaderManager.initLoader(LOADER_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<StatisticsEntity>> {
        loader = StatisticsLoader(this)
        loader.forceLoad()
        return loader
    }

    override fun onLoadFinished(
        loader: Loader<List<StatisticsEntity>>,
        data: List<StatisticsEntity>?
    ) {
        if (data != null) {
            statisticsAdapter.setStatisticsData(data)
        } else {
            Toast.makeText(this, R.string.statistics_is_empty, Toast.LENGTH_LONG).show()
        }
    }

    override fun onLoaderReset(loader: Loader<List<StatisticsEntity>>) {
        loader.cancelLoad()
    }
}