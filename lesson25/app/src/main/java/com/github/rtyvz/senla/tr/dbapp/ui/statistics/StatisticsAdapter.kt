package com.github.rtyvz.senla.tr.dbapp.ui.statistics

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.github.rtyvz.senla.tr.dbapp.R
import com.github.rtyvz.senla.tr.dbapp.databinding.StatisticsItemBinding
import com.github.rtyvz.senla.tr.dbapp.models.StatisticsEntity

class StatisticsAdapter : RecyclerView.Adapter<StatisticsAdapter.StatisticsViewHolder>() {
    private val listStatistics = mutableListOf<StatisticsEntity>()

    class StatisticsViewHolder(private val binding: StatisticsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: StatisticsEntity) {
            binding.apply {
                val context = binding.root.context
                commentAvgRateTextView.text = String.format(
                    context.getString(R.string.statistics_item_avg_rate_label),
                    item.avgCommentsRate.toString()
                )
                commentsCountTextView.text = String.format(
                    context.getString(R.string.statistics_item_comment_count),
                    item.commentsCount.toString()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatisticsViewHolder {
        val view = StatisticsItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return StatisticsViewHolder(view)
    }

    override fun getItemCount() = listStatistics.size

    override fun onBindViewHolder(holder: StatisticsViewHolder, position: Int) {
        holder.bind(listStatistics[position])
    }

    fun setStatisticsData(data: List<StatisticsEntity>) {
        listStatistics.clear()
        listStatistics.addAll(data)
        notifyDataSetChanged()
    }
}