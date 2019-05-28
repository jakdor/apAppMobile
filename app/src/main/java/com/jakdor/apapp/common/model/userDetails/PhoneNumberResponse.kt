package com.jakdor.apapp.common.model.userDetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PhoneNumberResponse
constructor(@SerializedName("phoneNumber") @Expose var userPhoneNumber: String)