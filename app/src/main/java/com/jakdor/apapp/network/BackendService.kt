package com.jakdor.apapp.network

import com.jakdor.apapp.common.model.apartment.ApartmentAdd
import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.common.model.apartment.ApartmentListRequest
import com.jakdor.apapp.common.model.auth.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface BackendService {

    @POST("apartments")
    fun getApartments(@Body apartmentListRequest: ApartmentListRequest): Observable<ApartmentList?>

    @POST("apartments/add")
    fun addApartment(@Body apartment: ApartmentAdd): Observable<Int>

    @Multipart
    @POST("Pictures/{idAp}")
    fun addApartmentImage(@Path("idAp") apartmentId: Int, @Part image: MultipartBody.Part): Observable<ResponseBody>

    @POST("Auth/refresh")
    fun postRefresh(@Body refreshRequest: RefreshRequest): Observable<RefreshResponse?>

    @POST("Auth/login")
    fun postLogin(@Body loginRequest: LoginRequest): Observable<LoginResponse?>

    @POST("Auth/register")
    fun postRegister(@Body registerRequest: RegisterRequest): Observable<RegisterResponse?>

    companion object {
        const val API_URL = "http://159.65.168.123:50649/api/"
    }
}