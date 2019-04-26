package com.jakdor.apapp.ui.userPanel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.common.repository.ApartmentRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject

class UserPanelViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val apartmentRepository: ApartmentRepository):
    BaseViewModel(application, rxSchedulersFacade){

    val apartmentsListLiveData = MutableLiveData<ApartmentList>()

    fun observeApartmentsListSubject(){
        disposable.add(apartmentRepository.apartmentsListSubject
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: ApartmentList -> apartmentsListLiveData.postValue(t) },
                {e ->  Timber.e(e, "ERROR observing ApartmentsListSubject")}))
    }

    fun requestApartmentsListUpdate(){
        apartmentRepository.requestApartments(50, 0)
    }
}