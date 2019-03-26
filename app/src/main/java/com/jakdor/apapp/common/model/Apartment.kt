package com.jakdor.apapp.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Apartment
constructor(@SerializedName("name") @Expose var name: String,
            @SerializedName("city") @Expose var city: String,
            @SerializedName("street") @Expose var street: String,
            @SerializedName("apartment") @Expose var apartment: String,
            @SerializedName("imgList") @Expose var imgList: List <String>,
            @SerializedName("imgThumb") @Expose var imgThumb: String,
            @SerializedName("lat") @Expose var lat: Float,
            @SerializedName("long") @Expose var long: Float,
            @SerializedName("rating") @Expose var rating: Float)