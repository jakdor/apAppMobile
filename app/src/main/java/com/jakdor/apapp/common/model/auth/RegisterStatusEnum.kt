package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.SerializedName

enum class RegisterStatusEnum {
    @SerializedName("0") ERROR,
    @SerializedName("1") OK,
    @SerializedName("2") EMAIL_TAKEN,
    @SerializedName("3") LOGIN_TAKEN
}