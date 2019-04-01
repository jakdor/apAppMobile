package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class TokenStorageModel
constructor(@SerializedName("accessToken") @Expose var accessToken: String,
            @SerializedName("refreshToken") @Expose var refreshToken: String)