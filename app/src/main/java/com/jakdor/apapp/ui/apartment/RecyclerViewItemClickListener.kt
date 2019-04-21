package com.jakdor.apapp.ui.apartment

import android.view.View

interface RecyclerViewItemClickListener {
    fun onItemClick(view: View, position: Int)
    //fun onItemLongClick(view: View, position: Int)
}