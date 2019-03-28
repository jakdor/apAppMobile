package com.jakdor.apapp.ui.apartmentList

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.jakdor.apapp.databinding.ItemApartmentListBinding
import com.bumptech.glide.request.RequestOptions
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.utils.diffCallback.ApartmentItemDiffCallback
import java.util.Vector

class ApartmentItemAdapter
constructor(private val apartmentVector: Vector<Apartment>,
            private val glide: RequestManager): RecyclerView.Adapter<ApartmentItemAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemApartmentListBinding.inflate(inflater, parent, false)
        return ApartmentItemAdapter.Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = apartmentVector[position]
        holder.bind(item)

        glide.load(item.imgThumb)
            .apply(RequestOptions()
                    .fitCenter()
                    .placeholder(R.mipmap.ic_launcher_round)
            )
            .into(holder.binding.apartmentImage)
    }

    override fun getItemCount(): Int {
       return apartmentVector.count()
    }

    /**
    * Update adapter content
    */
    fun updateItems(newItemList: MutableList<Apartment>){
        val oldItemList = mutableListOf<Apartment>()
        oldItemList.addAll(apartmentVector)

        val handler = Handler()
        Thread(Runnable {
            val diffCallback = ApartmentItemDiffCallback(oldItemList, newItemList)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            handler.post {
                diffResult.dispatchUpdatesTo(this)
                apartmentVector.clear()
                apartmentVector.addAll(newItemList)
            }
        }).start()
    }

    /**
     * ApartmentItemAdapter view holder
     */
    class Holder
    constructor(val binding: ItemApartmentListBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(apartment: Apartment){
            binding.apartment = apartment
            binding.executePendingBindings()
        }
    }
}