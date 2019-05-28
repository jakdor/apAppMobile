package com.jakdor.apapp.common.model.rating

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RatingListResponse
constructor(@SerializedName("ratingsList") @Expose var ratingsList: List<Rating>?,
            @SerializedName("hasMore") @Expose var hasMore: Boolean)