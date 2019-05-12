package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshRequest
constructor(@SerializedName("Login") @Expose var login: String,
            @SerializedName("RefreshToken") @Expose var refreshToken: String)