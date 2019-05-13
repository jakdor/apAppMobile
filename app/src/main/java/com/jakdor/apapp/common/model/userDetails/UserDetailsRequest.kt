package com.jakdor.apapp.common.model.userDetails

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserDetailsRequest
(@SerializedName("userID") @Expose var userId: String?)