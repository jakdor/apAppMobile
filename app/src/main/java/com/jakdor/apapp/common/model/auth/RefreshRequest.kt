package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshRequest
constructor(@SerializedName("login") @Expose var login: String,
            @SerializedName("refreshToken") @Expose var refreshToken: String)