package com.jakdor.apapp.ui.apartmentDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.common.repository.ApartmentRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject

class ApartmentDetailsViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val apartmentRepository: ApartmentRepository):
    BaseViewModel(application, rxSchedulersFacade)
{

    val apartPhoneNumberLiveData = MutableLiveData<String>()

    fun getApartment(apartmentId: Int): Apartment?{
        return if(apartmentRepository.apartmentListCache != null){
            apartmentRepository.apartmentListCache!!.find { apartment -> apartment.id == apartmentId }
        } else{
            null
        }
    }

    fun observeApartmentPhoneNumber(){
        disposable.add(apartmentRepository.apartmentPhoneNumber
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({t: String -> apartPhoneNumberLiveData.postValue(t)},
                {e-> Timber.e(e,"ERROR observing phone number")}))
    }

    fun getApartmentPhoneNumber(id: Int){
        apartmentRepository.getApartmentPhoneNumber(id)
    }

    fun convert(number: Int) : String{

        var numberAsString = String.format("%.2f",(number/100.0))

        numberAsString = numberAsString.replace('.',',')

        numberAsString += " zł"

        return numberAsString
    }
}