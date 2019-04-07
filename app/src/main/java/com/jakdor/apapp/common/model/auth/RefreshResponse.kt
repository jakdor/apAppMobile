package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RefreshResponse
constructor(@SerializedName("access_token") @Expose var accessToken: String)