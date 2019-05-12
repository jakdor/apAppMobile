package com.jakdor.apapp.common.model.apartment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApartmentAdd
constructor(@SerializedName("Name") @Expose var name: String,
            @SerializedName("City") @Expose var city: String,
            @SerializedName("Street") @Expose var street: String,
            @SerializedName("ApartmentNumber") @Expose var apartment: String,
            @SerializedName("Lat") @Expose var lat: Float,
            @SerializedName("Long") @Expose var long: Float)