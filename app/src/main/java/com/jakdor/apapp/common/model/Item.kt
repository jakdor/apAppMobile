package com.jakdor.apapp.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Item
constructor(@SerializedName("title") @Expose var title: String,
            @SerializedName("link") @Expose var link: String,
            @SerializedName("owner") @Expose var owner: Owner
)