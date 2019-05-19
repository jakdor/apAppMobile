package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.apartment.ApartmentAdd
import com.jakdor.apapp.common.model.auth.ApartmentAddResponse
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.BearerAuthWrapper
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class AddApartmentRepository
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val bearerAuthWrapper: BearerAuthWrapper,
                    private val rxSchedulersFacade: RxSchedulersFacade) {

    private val apiService: BackendService = retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val apartmentIdSubject: BehaviorSubject<ApartmentAddResponse> = BehaviorSubject.create()

    val sendingImages: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun addApartment(name: String, city: String, street: String, apartmentNumber: String, lat: Float, long: Float){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.addApartment(ApartmentAdd(name, city, street, apartmentNumber, lat, long)))
                .observeOn(rxSchedulersFacade.io())
                .subscribeOn(rxSchedulersFacade.io())
                .subscribe({ t: ApartmentAddResponse? -> if(t!=null) apartmentIdSubject.onNext(t)},
                    { e -> Timber.e(e, "ERROR adding Apartment") })
        )
    }

    fun addApartmentImage(apartmentId: Int, image: MultipartBody.Part){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.addApartmentImage(apartmentId,image))
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({t: ResponseBody -> Timber.d("Success: %s", t.toString())},
                { e -> Timber.e(e, "ERROR adding Apartment image"); sendingImages.onNext(false) },
                {sendingImages.onNext(true)})
        )
    }

    fun dispose() {
        if (!rxDisposables.isDisposed) rxDisposables.dispose()
    }
}