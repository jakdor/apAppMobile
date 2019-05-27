package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserPhoneNumberResponse
constructor(@SerializedName("phoneNumber") @Expose var userPhoneNumber: String)