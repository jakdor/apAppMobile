package com.jakdor.apapp.common.model.apartmentAdd

import com.google.gson.annotations.SerializedName

enum class ApartmentAddStatusEnum {
    @SerializedName("0") ERROR,
    @SerializedName("1") OK,
    @SerializedName("2") APARTMENT_EXISTS
}