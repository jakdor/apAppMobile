package com.jakdor.apapp.utils.diffCallback

import androidx.recyclerview.widget.DiffUtil
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.ui.apartmentList.ApartmentItemAdapter

/**
 * Difference callback for smart reloading of [ApartmentItemAdapter] content
 */
class ApartmentItemDiffCallback(private val oldItems: List<Apartment>,
                                private val newItems: List<Apartment>): DiffUtil.Callback() {

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