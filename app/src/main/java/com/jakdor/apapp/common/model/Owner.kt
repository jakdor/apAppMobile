package com.jakdor.apapp.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Owner
constructor(@SerializedName("display_name") @Expose var displayName: String,
            @SerializedName("profile_image") @Expose var profileImage: String?)