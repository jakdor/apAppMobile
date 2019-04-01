package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest
constructor(@SerializedName("login") @Expose var login: String,
            @SerializedName("password") @Expose var password: String)