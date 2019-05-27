package com.jakdor.apapp.common.model.apartmentAdd

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApartmentAddResponse
constructor(@SerializedName("id") @Expose var id: Int,
            @SerializedName("UploadStatus") @Expose var apartmentAddStatus: ApartmentAddStatusEnum)