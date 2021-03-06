package com.jakdor.apapp.network

import com.jakdor.apapp.common.model.apartment.ApartmentAdd
import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.common.model.apartment.ApartmentListRequest
import com.jakdor.apapp.common.model.apartmentAdd.ApartmentAddResponse
import com.jakdor.apapp.common.model.auth.*
import com.jakdor.apapp.common.model.rating.RatingListResponse
import com.jakdor.apapp.common.model.userDetails.PhoneNumberResponse
import com.jakdor.apapp.common.model.userDetails.UserDetails
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.*

interface BackendService {

    @GET("User/details")
    fun getUserDetails() : Observable<UserDetails?>

    @POST("apartments")
    fun getApartments(@Body apartmentListRequest: ApartmentListRequest): Observable<ApartmentList?>

    @GET("apartments/phoneNumber")
    fun getApartmentPhoneNumber(@Query("id") id: Int): Observable<PhoneNumberResponse>

    @POST("apartments/add")
    fun addApartment(@Body apartment: ApartmentAdd): Observable<ApartmentAddResponse?>

    @Multipart
    @POST("Pictures/{idAp}")
    fun addApartmentImage(@Path("idAp") apartmentId: Int, @Part image: MultipartBody.Part,
                          @Query("isThumb") isThumb: Boolean): Observable<ResponseBody>

    @GET("User/phoneNumber")
    fun getUserPhoneNumber(): Observable<PhoneNumberResponse>

    @POST("Auth/refresh")
    fun postRefresh(@Body refreshRequest: RefreshRequest): Observable<RefreshResponse?>

    @POST("Auth/login")
    fun postLogin(@Body loginRequest: LoginRequest): Observable<LoginResponse?>

    @POST("Auth/register")
    fun postRegister(@Body registerRequest: RegisterRequest): Observable<RegisterResponse?>

    @GET("Ratings")
    fun getRatings(@Query("idAp") idAp: Int,
                   @Query("limit") limit: Int,
                   @Query("offset") offset: Int): Observable<RatingListResponse>

    companion object {
        const val API_URL = "http://167.99.60.13:50649/api/"
    }
}