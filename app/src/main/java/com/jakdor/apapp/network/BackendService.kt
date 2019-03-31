package com.jakdor.apapp.network

import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.common.model.apartment.ApartmentListRequest
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendService {

    @POST("apartments")
    fun getApartments(@Body apartmentListRequest: ApartmentListRequest): Observable<ApartmentList?>

    companion object {
        const val API_URL = "http://159.65.168.123:50649/api/"
    }
}