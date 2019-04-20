package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.apartment.ApartmentAdd
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.BearerAuthWrapper
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class AddApartmentRepository
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val bearerAuthWrapper: BearerAuthWrapper,
                    private val rxSchedulersFacade: RxSchedulersFacade
) {

    private val apiService: BackendService = retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    fun addApartment(name: String, city: String, street: String, apartmentNumber: String) {
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.addApartment(ApartmentAdd(name, city, street, apartmentNumber)))
                .observeOn(rxSchedulersFacade.io())
                .subscribeOn(rxSchedulersFacade.io())
                .subscribe({ },
                    { e -> Timber.e(e, "ERROR adding Apartment") })
        )
    }

    fun dispose() {
        if (!rxDisposables.isDisposed) rxDisposables.dispose()
    }
}