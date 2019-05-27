package com.jakdor.apapp.common.model.rating

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Rating
constructor(@SerializedName("ID_Rating") @Expose var idRating: Int,
            @SerializedName("Owner") @Expose var ownerRating: Int,
            @SerializedName("Location") @Expose var locationRating: Int,
            @SerializedName("Price") @Expose var priceRating: Int,
            @SerializedName("Description") @Expose var description: String)