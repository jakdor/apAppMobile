package com.jakdor.apapp.common.model.apartment

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApartmentListRequest
constructor(@SerializedName("limit") @Expose var limit: Int,
            @SerializedName("offset") @Expose var offset: Int)