package com.jakdor.apapp.network

import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.common.model.apartment.ApartmentListRequest
import com.jakdor.apapp.common.model.auth.LoginRequest
import com.jakdor.apapp.common.model.auth.LoginResponse
import com.jakdor.apapp.common.model.auth.RefreshRequest
import com.jakdor.apapp.common.model.auth.RefreshResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

interface BackendService {

    @POST("apartments")
    fun getApartments(@Body apartmentListRequest: ApartmentListRequest): Observable<ApartmentList?>

    @POST("Auth/refresh")
    fun postRefresh(@Body refreshRequest: RefreshRequest): Observable<RefreshResponse?>

    @POST("Auth/login")
    fun postLogin(@Body loginRequest: LoginRequest): Observable<LoginResponse?>

    companion object {
        const val API_URL = "http://159.65.168.123:50649/api/"
    }
}