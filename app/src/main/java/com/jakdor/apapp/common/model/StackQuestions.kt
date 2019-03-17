package com.jakdor.apapp.common.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class StackQuestions
constructor(@SerializedName("items") @Expose var items: List<Item>?,
            @SerializedName("quota_remaining") @Expose var quotaRemaining: Int)