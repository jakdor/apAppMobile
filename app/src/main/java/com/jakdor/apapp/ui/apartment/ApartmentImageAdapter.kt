package com.jakdor.apapp.ui.apartment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.jakdor.apapp.R
import java.util.*

class ApartmentImageAdapter(private val glide: RequestManager, private val imagesList: ArrayList<Picture>):
    RecyclerView.Adapter<ApartmentImageAdapter.Holder>() {

    lateinit var recyclerViewItemClickListener: RecyclerViewItemClickListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)

        return Holder(inflater.inflate(R.layout.image_apartment_list, parent, false))
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemPosition = position
        val item = imagesList[position].image

        glide.load(item)
            .apply(
                RequestOptions()
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher_round)
            )
            .centerCrop()
            .into(holder.imageView)
    }

    fun setOnItemClickListener(recyclerViewItemClickListener: RecyclerViewItemClickListener) {
        this.recyclerViewItemClickListener = recyclerViewItemClickListener
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.apartment_image)
        var cardView: CardView = itemView.findViewById(R.id.image_cardView)
        var deleteButton: Button = itemView.findViewById(R.id.delete_image)
        var itemPosition: Int = 0

        var secondClick: Boolean = false

        init {
            cardView.setOnClickListener {
                //recyclerViewItemClickListener.onItemClick(itemView, itemPosition)
                if(!secondClick) {
                    deleteButton.visibility = View.VISIBLE
                    secondClick = true
                }else{
                    deleteButton.visibility = View.GONE
                    secondClick = false
                }
            }
            deleteButton.setOnClickListener{ recyclerViewItemClickListener.onItemClick(itemView, itemPosition)}
        }


    }
}