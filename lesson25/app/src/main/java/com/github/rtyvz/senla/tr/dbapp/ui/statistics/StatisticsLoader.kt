package com.github.rtyvz.senla.tr.dbapp.ui.statistics

import android.content.Context
import androidx.loader.content.AsyncTaskLoader
import com.github.rtyvz.senla.tr.dbapp.db.helper.DbHelper
import com.github.rtyvz.senla.tr.dbapp.models.StatisticsEntity
import com.github.rtyvz.senla.tr.dbapp.provider.DbProvider

class StatisticsLoader(context: Context) : AsyncTaskLoader<List<StatisticsEntity>>(context) {
    override fun loadInBackground(): List<StatisticsEntity> {
        return DbHelper().getStatistics(DbProvider.provideDb().writableDatabase)
    }
}