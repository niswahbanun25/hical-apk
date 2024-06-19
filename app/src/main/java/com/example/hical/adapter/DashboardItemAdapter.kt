package com.example.hical.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hical.databinding.ItemDashboardRowBinding
import com.example.hical.response.DashboardItem

class DashboardItemAdapter :
    ListAdapter<DashboardItem, DashboardItemAdapter.MyViewHolder>(DIFF_CALLBACK) {
    var onItemDeleteClick: ((DashboardItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        ItemDashboardRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    inner class MyViewHolder(private val binding: ItemDashboardRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DashboardItem) {
            with(binding) {
                tvTitle.text = item.meal
                tvCal.text = item.amount.toString()

                root.setOnClickListener {
                    onItemDeleteClick?.invoke(item)
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DashboardItem>() {
            override fun areItemsTheSame(
                oldItem: DashboardItem,
                newItem: DashboardItem
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: DashboardItem,
                newItem: DashboardItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}