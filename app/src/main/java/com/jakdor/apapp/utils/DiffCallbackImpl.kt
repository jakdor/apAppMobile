package com.jakdor.apapp.utils

import androidx.recyclerview.widget.DiffUtil

/**
 * Difference callback for smart reloading of RecyclerViewAdapter content
 */
class DiffCallbackImpl<T>(private val oldItems: List<T>,
                          private val newItems: List<T>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition] == newItems[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldItems.size
    }

    override fun getNewListSize(): Int {
        return newItems.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldItems[oldItemPosition].toString() == newItems[newItemPosition].toString()
    }
}