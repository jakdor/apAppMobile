package com.jakdor.apapp.ui.apartment

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.repository.AddApartmentRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import java.util.regex.Pattern
import javax.inject.Inject

class ApartmentViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val addApartmentRepository: AddApartmentRepository):
                    BaseViewModel(application, rxSchedulersFacade){

    val addApartmentPossibility = MutableLiveData<Boolean>().apply { value = false }

    val apartmentIdLiveData = MutableLiveData<Int>()

    private var isNameCorrect: Boolean = false
    private var isCityCorrect: Boolean = false
    private var isStreetCorrect: Boolean = false
    private var isApartmentNumberCorrect: Boolean = true

    val apartmentNameStatus = MutableLiveData<Boolean>().apply { value = false }
    val apartmentCityStatus = MutableLiveData<Boolean>().apply { value = false }
    val apartmentStreetStatus = MutableLiveData<Boolean>().apply { value = false }
    val apartmentNumberStatus = MutableLiveData<ApartmentNumberStatus>().apply { value = ApartmentNumberStatus.WRONG_PATTERN }

    fun observeApartmentIdSubject(){
        disposable.add(addApartmentRepository.apartmentIdSubject
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: Int -> apartmentIdLiveData.postValue(t) },
                {e ->  Timber.e(e, "ERROR observing ApartmentIdSubject")}))
    }

    fun addApartment(name: String, city: String, street: String, apartmentNumber: String, lat: Float, long: Float){
        addApartmentRepository.addApartment(name,city,street,apartmentNumber, lat, long)
    }

    fun getLatLng(context: FragmentActivity?, address: String): LatLng?{

        val geocoder = Geocoder(context)

        val address: List<Address> = geocoder.getFromLocationName(address,1)

        if(address.isNotEmpty()){
            val longitude: Float = address[0].longitude.toFloat()
            val latitude: Float = address[0].latitude.toFloat()

            return LatLng(latitude, longitude)
        }

        return null
    }

    fun apartmentNameValidation(apartmentName: String){

        isNameCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        if(apartmentName.trim().isEmpty()){
            isNameCorrect = false
            apartmentNameStatus.postValue(false)
        }else{
            isNameCorrect = true
            apartmentNameStatus.postValue(true)
        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)
    }

    fun apartmentCityValidation(apartmentCity: String){

        isCityCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        if(apartmentCity.trim().isEmpty()){
            isCityCorrect = false
            apartmentCityStatus.postValue(false)
        }else{
            isCityCorrect = true
            apartmentCityStatus.postValue(true)
        }


        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)
    }

    fun apartmentStreetValidation(apartmentStreet: String){

        isStreetCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        if(apartmentStreet.trim().isEmpty()){
            isStreetCorrect = false
            apartmentStreetStatus.postValue(false)
        }else{
            isStreetCorrect = true
            apartmentStreetStatus.postValue(true)
        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)
    }

    fun apartmentNumberValidation(apartmentNumber: String){
        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

        val apartmentNumberPattern: Pattern = Pattern.compile("^([0-9]+)([/])([0-9]+\$)")
        val apartmentNumberPattern2: Pattern = Pattern.compile("^([0-9]+\$)")

        if(apartmentNumber.trim().isNotEmpty()){
            if(!apartmentNumberPattern.matcher(apartmentNumber).find()){
                if(apartmentNumberPattern2.matcher(apartmentNumber).find()){
                    isApartmentNumberCorrect = true
                    apartmentNumberStatus.postValue(ApartmentNumberStatus.OK)
                    addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                            isApartmentNumberCorrect)
                    return
                }
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

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect)

    }

    enum class ApartmentNumberStatus {
        OK, EMPTY, WRONG_PATTERN
    }

    data class LatLng(var latitude: Float, var longitude: Float)
}