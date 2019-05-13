package com.jakdor.apapp.common.model.apartment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Apartment
constructor(@SerializedName("ID_Ap") @Expose var id: Int,
            @SerializedName("Name") @Expose var name: String,
            @SerializedName("City") @Expose var city: String,
            @SerializedName("Street") @Expose var street: String,
            @SerializedName("ApartmentNumber") @Expose var apartmentNum: String,
            @SerializedName("ImgList") @Expose var imgList: List <String>,
            @SerializedName("ImgThumb") @Expose var imgThumb: String,
            @SerializedName("Lat") @Expose var lat: Float,
            @SerializedName("Long") @Expose var long: Float,
            @SerializedName("Rate") @Expose var rating: Float?,
            @SerializedName("Description") @Expose var description: String)