package com.jakdor.apapp.network

import com.jakdor.apapp.common.model.apartment.Apartment
import io.reactivex.Observable
import retrofit2.http.GET

interface BackendService {

    @GET("apartments")
    fun getApartments(): Observable<List<Apartment>?>

    companion object {
        const val API_URL = "http://134.209.231.199:3000/api/"
    }
}