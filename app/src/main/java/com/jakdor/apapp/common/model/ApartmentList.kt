package com.jakdor.apapp.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApartmentList
constructor(@SerializedName("apartments") @Expose var apartments: List<Apartment>?)