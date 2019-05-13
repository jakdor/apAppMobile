package com.jakdor.apapp.utils.diffCallback

import androidx.recyclerview.widget.DiffUtil
import com.jakdor.apapp.ui.apartment.Picture

class ApartmentImageDiffCallback(private val oldImagesList: ArrayList<Picture>,
                                 private val newImagesList: ArrayList<Picture>): DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldImagesList[oldItemPosition] == newImagesList[newItemPosition]
    }

    override fun getOldListSize(): Int {
        return oldImagesList.size
    }

    override fun getNewListSize(): Int {
        return newImagesList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldImagesList[oldItemPosition].picturePath == newImagesList[newItemPosition].picturePath
    }

}
