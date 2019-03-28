package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

/**
 * Apartment repository
 */
class ApartmentRepository
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val rxSchedulersFacade: RxSchedulersFacade){

    private val apiService: BackendService =
        retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val apartmentsListSubject: BehaviorSubject<List<Apartment>> = BehaviorSubject.create()

    fun requestApartments(){
        rxDisposables.add(apiService.getApartments()
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: List<Apartment>? -> if(t != null) apartmentsListSubject.onNext(t) },
                {e ->  Timber.e(e, "Error observing Apartments")}))
    }

    fun dispose(){
        if(!rxDisposables.isDisposed) rxDisposables.dispose()
    }
}