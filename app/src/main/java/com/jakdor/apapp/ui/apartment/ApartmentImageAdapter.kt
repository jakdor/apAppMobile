package com.jakdor.apapp.ui.apartment

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jakdor.apapp.R
import com.jakdor.apapp.utils.diffCallback.ApartmentImageDiffCallback
import java.util.*
import kotlin.collections.ArrayList

class ApartmentImageAdapter(private val glide: RequestManager, private val imagesList: ArrayList<Picture>,
                            val context: Context):
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
        val item = imagesList[position].image

        glide
            .load(item)
            .centerCrop()
            .into(holder.imageView)

        var secondClick = false

        holder.cardView.setOnClickListener {
            if(!secondClick) {
                holder.deleteButton.visibility = View.VISIBLE
                if(position != 0) {
                    holder.thumbnailButton.visibility = View.VISIBLE
                }
                secondClick = true
            }else{
                holder.deleteButton.visibility = View.GONE
                if(position != 0) {
                    holder.thumbnailButton.visibility = View.GONE
                }
                secondClick = false
            }
        }
        holder.deleteButton.setOnClickListener{
            holder.deleteButton.visibility = View.GONE
            if(position != 0) {
                holder.thumbnailButton.visibility = View.GONE
            }
            recyclerViewItemClickListener.onItemClick(holder.itemView, position)
        }

        holder.thumbnailButton.setOnClickListener{
            holder.deleteButton.visibility = View.GONE
            if(position != 0) {
                holder.thumbnailButton.visibility = View.GONE
            }
            recyclerViewItemClickListener.changeThumbnail(position)
        }
    }

    fun updateItems(newImagesList: ArrayList<Picture>){
        val oldImagesList = arrayListOf<Picture>()
        oldImagesList.addAll(imagesList)

        val handler = Handler()

        Thread(Runnable {
            val diffCallback = ApartmentImageDiffCallback(oldImagesList, newImagesList)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            handler.post {
                diffResult.dispatchUpdatesTo(this)
                imagesList.clear()
                imagesList.addAll(newImagesList)
            }
        }).start()

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var imageView: ImageView = itemView.findViewById(R.id.apartment_image)
        var cardView: CardView = itemView.findViewById(R.id.image_cardView)
        var deleteButton: Button = itemView.findViewById(R.id.delete_image)
        var thumbnailButton: Button = itemView.findViewById(R.id.thumbnail_button)
    }

    interface RecyclerViewItemClickListener {
        fun onItemClick(view: View, position: Int)
        fun changeThumbnail(position: Int)
    }
}