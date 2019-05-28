package com.jakdor.apapp.common.model.apartment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Apartment
constructor(@SerializedName("ID_Ap") @Expose var id: Int,
            @SerializedName("Name") @Expose var name: String,
            @SerializedName("City") @Expose var city: String,
            @SerializedName("Street") @Expose var street: String,
            @SerializedName("ApartmentNumber") @Expose var apartmentNum: String,
            @SerializedName("ImgList") @Expose var imgList: List <String>?,
            @SerializedName("ImgThumb") @Expose var imgThumb: String,
            @SerializedName("Price") @Expose var price: Int,
            @SerializedName("MaxPeople") @Expose var maxPeople: Int,
            @SerializedName("Area") @Expose var area: Int,
            @SerializedName("PhoneNumber") @Expose var phoneNumber: String,
            @SerializedName("Lat") @Expose var lat: Float,
            @SerializedName("Long") @Expose var long: Float,
            @SerializedName("Description") @Expose var description: String,
            @SerializedName("OwnerRating") @Expose var ownerRating: Float,
            @SerializedName("LocationRating") @Expose var locationRating: Float,
            @SerializedName("StandardRating") @Expose var standardRating: Float,
            @SerializedName("PriceRating") @Expose var priceRating: Float)