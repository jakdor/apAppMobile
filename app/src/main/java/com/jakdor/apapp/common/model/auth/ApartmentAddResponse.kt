package com.jakdor.apapp.common.model.auth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ApartmentAddResponse
    constructor(@SerializedName("id") @Expose var id: Int,
                @SerializedName("UploadStatus") @Expose var apartmentAddStatus: ApartmentAddStatusEnum)