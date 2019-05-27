package com.jakdor.apapp.ui.apartmentDetails

import android.os.Handler
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jakdor.apapp.common.model.rating.Rating
import com.jakdor.apapp.common.model.rating.RatingDisplayModel
import com.jakdor.apapp.databinding.ItemRatingListBinding
import com.jakdor.apapp.utils.DiffCallbackImpl
import java.util.*

class RatingItemAdapter
constructor(private val apartmentVector: Vector<Rating>): RecyclerView.Adapter<RatingItemAdapter.Holder>() {

    var recyclerViewItemClickListener: RecyclerViewItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemRatingListBinding.inflate(inflater, parent, false)
        return Holder(itemBinding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = apartmentVector[position]
        holder.bind(formatRatingDisplayModel(item))

        holder.binding.apartmentItem.setOnClickListener{
            recyclerViewItemClickListener?.onItemClick(item.idRating)
        }
    }

    override fun getItemCount(): Int {
        return apartmentVector.count()
    }

    /**
     * Update adapter content
     */
    fun updateItems(newItemList: MutableList<Rating>){
        val oldItemList = mutableListOf<Rating>()
        oldItemList.addAll(apartmentVector)

        val handler = Handler()
        Thread(Runnable {
            val diffCallback = DiffCallbackImpl(oldItemList, newItemList)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            handler.post {
                diffResult.dispatchUpdatesTo(this)
                apartmentVector.clear()
                apartmentVector.addAll(newItemList)
            }
        }).start()
    }

    private fun formatRatingDisplayModel(ratingModel: Rating): RatingDisplayModel{
        val avg = (ratingModel.locationRating + ratingModel.ownerRating
                + ratingModel.priceRating + ratingModel.standardRating) / 4.0f

        return RatingDisplayModel(
            ratingModel.idRating,
            avg,
            ratingModel.ownerRating,
            ratingModel.locationRating,
            ratingModel.standardRating,
            ratingModel.priceRating,
            ratingModel.description,
            ratingModel.login,
            "%.2f".format(avg))
    }

    /**
     * ApartmentItemAdapter view holder
     */
    class Holder
    constructor(val binding: ItemRatingListBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rating: RatingDisplayModel){
            binding.rating = rating
            binding.executePendingBindings()
        }
    }

    interface RecyclerViewItemClickListener {
        fun onItemClick(ratingId: Int)
    }
}