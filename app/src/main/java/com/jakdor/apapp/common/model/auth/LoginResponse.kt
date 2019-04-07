package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse
constructor(@SerializedName("access_token") @Expose var accessToken: String?,
            @SerializedName("refresh_token") @Expose var refreshToken: String?,
            @SerializedName("error") @Expose var error: String?)