package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.common.model.apartment.ApartmentListRequest
import com.jakdor.apapp.common.model.auth.PhoneNumberResponse
import com.jakdor.apapp.network.BearerAuthWrapper
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * Apartment repository
 */
class ApartmentRepository
@Inject constructor(private val bearerAuthWrapper: BearerAuthWrapper,
                    private val rxSchedulersFacade: RxSchedulersFacade){


    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val apartmentsListSubject: BehaviorSubject<ApartmentList> = BehaviorSubject.create()
    val apartmentPhoneNumber: BehaviorSubject<String> = BehaviorSubject.create()

    var apartmentListCache: List<Apartment>? = null

    fun requestApartments(limit: Int, offset: Int){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.getApartments(ApartmentListRequest(limit, offset)))
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: ApartmentList? -> if(t != null){
                apartmentsListSubject.onNext(t)
                apartmentListCache = t.apartments
            }},{e -> run {
                apartmentsListSubject.onNext(ApartmentList(null, false))
                Timber.e(e, "ERROR observing ApartmentsListSubject")
            }}))
    }

    fun getApartmentPhoneNumber(id: Int){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.getApartmentPhoneNumber(id))
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe ({ t: PhoneNumberResponse -> apartmentPhoneNumber.onNext(t.userPhoneNumber)},
                {e -> Timber.e(e,"ERROR getting user phone number")})
        )
    }

    fun dispose(){
        if(!rxDisposables.isDisposed) rxDisposables.dispose()
    }
}