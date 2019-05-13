package com.jakdor.apapp.common.model.userDetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.jakdor.apapp.common.model.apartment.Apartment

data class UserDetails
constructor(@SerializedName("user") @Expose var user: User,
            @SerializedName("personalData") @Expose var personalData: PersonalData,
            @SerializedName("apartmentList") @Expose var apartments: List<Apartment>?)