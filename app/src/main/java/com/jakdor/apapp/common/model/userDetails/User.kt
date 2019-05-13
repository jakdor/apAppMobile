package com.jakdor.apapp.common.model.userDetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User
constructor(@SerializedName("ID_User") @Expose var id: String,
            @SerializedName("Login") @Expose var login: String,
            @SerializedName("Email") @Expose var email: String,
            @SerializedName("Rate") @Expose var rate: Double,
            @SerializedName("isBlocked") @Expose var isBlocked: Boolean,
            @SerializedName("IDRole") @Expose var role: Int)