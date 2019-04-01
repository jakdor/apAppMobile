package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class RegisterResponse
constructor(@SerializedName("RegisterStatus") @Expose var registerStatus: RegisterStatusEnum)