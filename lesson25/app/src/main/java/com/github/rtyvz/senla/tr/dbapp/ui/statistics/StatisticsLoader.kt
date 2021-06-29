package com.github.rtyvz.senla.tr.dbapp.ui.statistics

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.github.rtyvz.senla.tr.dbapp.db.DbHelper
import com.github.rtyvz.senla.tr.dbapp.models.StatisticsEntity
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider

class StatisticsLoader(context: Context) : AsyncTaskLoader<List<StatisticsEntity>>(context) {
    override fun loadInBackground(): List<StatisticsEntity> {
        val list = DbHelper().getStatistics(DbProvider.provideDb().writableDatabase)
        return list
    }

}