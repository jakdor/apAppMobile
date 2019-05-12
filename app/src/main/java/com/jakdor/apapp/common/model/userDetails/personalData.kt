package com.jakdor.apapp.common.model.userDetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PersonalData
constructor(@SerializedName("ID_PData") @Expose var id: Int,
            @SerializedName("FirstName") @Expose var firstName: String,
            @SerializedName("LastName") @Expose var lastName: String,
            @SerializedName("BirthDate") @Expose var birthDate: String,
            @SerializedName("IDUser") @Expose var idUser: String)