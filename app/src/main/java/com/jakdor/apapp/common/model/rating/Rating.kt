package com.jakdor.apapp.common.model.rating

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Rating
constructor(@SerializedName("ID_Rating") @Expose var idRating: Int,
            @SerializedName("Owner") @Expose var ownerRating: Float,
            @SerializedName("Location") @Expose var locationRating: Float,
            @SerializedName("Standard") @Expose var standardRating: Float,
            @SerializedName("Price") @Expose var priceRating: Float,
            @SerializedName("Login") @Expose var login: String,
            @SerializedName("Description") @Expose var description: String)