package com.jakdor.apapp.ui.apartmentList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.common.repository.ApartmentRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject

class ApartmentListViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val apartmentRepository: ApartmentRepository):
    BaseViewModel(application, rxSchedulersFacade){

    val apartmentsListLiveData = MutableLiveData<List<Apartment>>()

    fun observeApartmentsListSubject(){
        disposable.add(apartmentRepository.apartmentsListSubject
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: List<Apartment> -> apartmentsListLiveData.postValue(t) },
                {e ->  Timber.e(e, "Error observing ApartmentsListSubject")}))
    }

    fun requestApartmentsListUpdate(){
        apartmentRepository.requestApartments()
    }
}