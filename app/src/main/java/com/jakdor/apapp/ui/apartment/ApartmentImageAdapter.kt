package com.jakdor.apapp.ui.apartment

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.jakdor.apapp.R
import java.util.*

class ApartmentImageAdapter(private val glide: RequestManager, private val imagesList: ArrayList<Bitmap>):
    RecyclerView.Adapter<ApartmentImageAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)

        return Holder(inflater.inflate(R.layout.image_apartment_list, parent, false))
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = imagesList[position]

        glide.load(item)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher_round)
            )
            .centerCrop()
            .into(holder.imageView)
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.apartment_image)


    }
}