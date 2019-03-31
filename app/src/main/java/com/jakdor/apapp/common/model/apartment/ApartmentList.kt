package com.jakdor.apapp.common.model.apartment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApartmentList
constructor(@SerializedName("apartmentsList") @Expose var apartments: List<Apartment>?,
            @SerializedName("hasMore") @Expose var hasMore: Boolean)