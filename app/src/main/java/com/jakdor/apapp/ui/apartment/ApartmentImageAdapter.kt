package com.jakdor.apapp.ui.apartment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.jakdor.apapp.R
import kotlinx.android.synthetic.main.image_apartment_list.view.*
import pl.aprilapps.easyphotopicker.MediaFile


class ApartmentImageAdapter
constructor(private val glide: RequestManager, private val imagesList: List<MediaFile>):
    RecyclerView.Adapter<ApartmentImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)

        return Holder(inflater.inflate(R.layout.image_apartment_list,parent,false))
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = imagesList[position]

        glide.load(item.file)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher_round)
            )
            .into(holder.imageView)
    }

    class Holder
    constructor(val view: View):
        RecyclerView.ViewHolder(view) {

        var imageView = view.apartment_image
    }
}