package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.apartment.ApartmentAdd
import com.jakdor.apapp.common.model.apartmentAdd.ApartmentAddResponse
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.BearerAuthWrapper
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import timber.log.Timber
import javax.inject.Inject

class AddApartmentRepository
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val bearerAuthWrapper: BearerAuthWrapper,
                    private val rxSchedulersFacade: RxSchedulersFacade) {

    private val apiService: BackendService = retrofitFactory.createService(BackendService.API_URL,
        BackendService::class.java)

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val apartmentIdSubject: BehaviorSubject<ApartmentAddResponse> = BehaviorSubject.create()

    val sendingImages: BehaviorSubject<Boolean> = BehaviorSubject.create()

    fun addApartment(name: String, city: String, street: String, apartmentNumber: String, price: Int, maxPeople: Int,
                     area: Int, phoneNumber: String, lat: Float, long: Float){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.addApartment(ApartmentAdd(name, city, street, apartmentNumber, price,
                maxPeople, area, phoneNumber, lat, long)))
                .observeOn(rxSchedulersFacade.io())
                .subscribeOn(rxSchedulersFacade.io())
                .subscribe({ t: ApartmentAddResponse? -> if(t!=null) apartmentIdSubject.onNext(t)},
                    { e -> Timber.e(e, "ERROR adding Apartment") })
        )
    }

    fun addApartmentImage(apartmentId: Int, image: MultipartBody.Part, isThumb: Boolean){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.addApartmentImage(apartmentId,image, isThumb))
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