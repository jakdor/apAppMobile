package com.jakdor.apapp.ui.apartmentDetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.jakdor.apapp.R

class ApartmentImgPagerAdapter
constructor(private val imgList: List <String>?,
            private val glide: RequestManager,
            context: Context) : PagerAdapter() {

    private val inflater = LayoutInflater.from(context)

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener){
        onItemClickListener = listener
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = inflater.inflate(R.layout.item_apertment_img, container, false)

        val imageView = view.findViewById<ImageView>(R.id.item_apartment_image)

        val url = imgList?.get(position)

        if(url != null) {
            glide.load(url)
                .apply(
                    RequestOptions().centerCrop()
                )
                .into(imageView)
        }

        imageView.setOnClickListener{
            v -> onItemClickListener?.onItemClick(v, position)
        }

        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }

    override fun getCount(): Int {
        return imgList?.count() ?: 0
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    interface OnItemClickListener {
        fun onItemClick(view: View, position: Int)
    }
}