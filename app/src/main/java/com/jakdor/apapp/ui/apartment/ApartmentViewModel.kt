package com.jakdor.apapp.ui.apartment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import java.util.regex.Pattern
import javax.inject.Inject

class ApartmentViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
                    BaseViewModel(application, rxSchedulersFacade){

    val addApartmentPossibilty = MutableLiveData<Boolean>().apply { value = false }

    private var isNameCorrect: Boolean = false
    private var isCityCorrect: Boolean = false
    private var isStreetCorrect: Boolean = false
    private var isApartmentNumberCorrect: Boolean = true

    val apartmentNameStatus = MutableLiveData<Boolean>().apply { value = false }
    val apartmentCityStatus = MutableLiveData<Boolean>().apply { value = false }
    val apartmentStreetStatus = MutableLiveData<Boolean>().apply { value = false }
    val apartmentNumberStatus = MutableLiveData<ApartmentNumberStatus>().apply { value = ApartmentNumberStatus.WRONG_PATTERN }

    fun apartmentNameValidation(apartmentName: String){

        isNameCorrect = false

        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        if(apartmentName.trim().isEmpty()){
            isNameCorrect = false
            apartmentNameStatus.postValue(false)
        }else{
            isNameCorrect = true
            apartmentNameStatus.postValue(true)
        }

        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)
    }

    fun apartmentCityValidation(apartmentCity: String){

        isCityCorrect = false

        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        if(apartmentCity.trim().isEmpty()){
            isCityCorrect = false
            apartmentCityStatus.postValue(false)
        }else{
            isCityCorrect = true
            apartmentCityStatus.postValue(true)
        }


        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)
    }

    fun apartmentStreetValidation(apartmentStreet: String){

        isStreetCorrect = false

        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        if(apartmentStreet.trim().isEmpty()){
            isStreetCorrect = false
            apartmentStreetStatus.postValue(false)
        }else{
            isStreetCorrect = true
            apartmentStreetStatus.postValue(true)
        }

        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)
    }

    fun apartmentNumberValidation(apartmentNumber: String){
        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        val apartmentNumberPattern: Pattern = Pattern.compile("^([0-9]+)([/])([0-9]+\$)")

        if(apartmentNumber.trim().isNotEmpty()){
            if(!apartmentNumberPattern.matcher(apartmentNumber).find()){
                isApartmentNumberCorrect = false
                apartmentNumberStatus.postValue(ApartmentNumberStatus.WRONG_PATTERN)
                return
            }
        }else{
            isApartmentNumberCorrect = false
            apartmentNumberStatus.postValue(ApartmentNumberStatus.EMPTY)
            return
        }

        isApartmentNumberCorrect = true
        apartmentNumberStatus.postValue(ApartmentNumberStatus.OK)

        addApartmentPossibilty.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

    }

    enum class ApartmentNumberStatus {
        OK, EMPTY, WRONG_PATTERN
    }
}