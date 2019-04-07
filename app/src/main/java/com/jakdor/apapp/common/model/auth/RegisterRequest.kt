package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterRequest
constructor(@SerializedName("Login") @Expose var login: String,
            @SerializedName("Email") @Expose var email: String,
            @SerializedName("Password") @Expose var password: String,
            @SerializedName("Name") @Expose var name: String,
            @SerializedName("Surname") @Expose var surname: String)