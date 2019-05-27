package com.jakdor.apapp.ui.apartment

import android.app.Application
import android.location.Address
import android.location.Geocoder
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.auth.ApartmentAddResponse
import com.jakdor.apapp.common.repository.AddApartmentRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import timber.log.Timber
import java.io.File
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.collections.ArrayList

class ApartmentViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val addApartmentRepository: AddApartmentRepository):
                    BaseViewModel(application, rxSchedulersFacade){

    val addApartmentPossibility = MutableLiveData<Boolean>().apply { value = false }

    val apartmentIdLiveData = MutableLiveData<ApartmentAddResponse>()
    val sentImagesLiveData = MutableLiveData<Boolean>()

    private var isNameCorrect: Boolean = false
    private var isCityCorrect: Boolean = false
    private var isStreetCorrect: Boolean = false
    private var isApartmentNumberCorrect: Boolean = false
    private var isPhoneNumberCorrect: Boolean = false
    private var isAreaCorrect: Boolean = true
    private var isMaxPeopleCorrect: Boolean = true

    val apartmentNameStatus = MutableLiveData<Boolean>().apply { value = true }
    val apartmentCityStatus = MutableLiveData<Boolean>().apply { value = true }
    val apartmentStreetStatus = MutableLiveData<Boolean>().apply { value = true }
    val apartmentMaxPeopleStatus = MutableLiveData<Boolean>().apply { value = true }
    val apartmentAreaStatus = MutableLiveData<Boolean>().apply { value = true }
    val userPhoneNumber = MutableLiveData<Status>().apply { value = Status.OK }
    val apartmentNumberStatus = MutableLiveData<Status>().apply { value = Status.OK }

    fun observeApartmentIdSubject(){
        disposable.add(addApartmentRepository.apartmentIdSubject
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: ApartmentAddResponse -> apartmentIdLiveData.postValue(t) },
                {e ->  Timber.e(e, "ERROR observing ApartmentIdSubject")}))
    }

    fun observeSendingImages(){
        disposable.add(addApartmentRepository.sendingImages
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({t: Boolean -> sentImagesLiveData.postValue(t)},
                {e-> Timber.e(e,"ERROR observing sending images")}))
    }

    fun addApartment(name: String, city: String, street: String, apartmentNumber: String, price: Int, maxPeople: Int,
                     area: Int, phoneNumber: String, lat: Float, long: Float){
        addApartmentRepository.addApartment(name, city, street, apartmentNumber, price, maxPeople, area, phoneNumber,
            lat, long)
    }

    fun addApartmentImage(apartmentId: Int, imageList: ArrayList<Picture>){
        for(image in imageList){
            val fileImage = File(image.picturePath)

            val requestBody = RequestBody.create(MediaType.parse("image/*"), fileImage)

            val filePart = MultipartBody.Part.createFormData("", fileImage.name, requestBody)

            addApartmentRepository.addApartmentImage(apartmentId, filePart)
        }
    }

    fun getLatLng(context: FragmentActivity?, address: String): LatLng?{

        val geocoder = Geocoder(context)

        val addressList: List<Address> = geocoder.getFromLocationName(address,1)

        if(addressList.isNotEmpty()){
            val longitude: Float = addressList[0].longitude.toFloat()
            val latitude: Float = addressList[0].latitude.toFloat()

            return LatLng(latitude, longitude)
        }

        return null
    }

    fun apartmentNameValidation(apartmentName: String){

        isNameCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        if(apartmentName.trim().isEmpty()){
            isNameCorrect = false
            apartmentNameStatus.postValue(false)
        }else{
            isNameCorrect = true
            apartmentNameStatus.postValue(true)
        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
    }

    fun apartmentCityValidation(apartmentCity: String){

        isCityCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        if(apartmentCity.trim().isEmpty()){
            isCityCorrect = false
            apartmentCityStatus.postValue(false)
        }else{
            isCityCorrect = true
            apartmentCityStatus.postValue(true)
        }


        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
    }

    fun apartmentStreetValidation(apartmentStreet: String){

        isStreetCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        if(apartmentStreet.trim().isEmpty()){
            isStreetCorrect = false
            apartmentStreetStatus.postValue(false)
        }else{
            isStreetCorrect = true
            apartmentStreetStatus.postValue(true)
        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
    }

    fun userPhoneNumberValidation(phoneNumber: String){

        isPhoneNumberCorrect = false

        val phoneNumberPattern: Pattern = Pattern.compile("^((\\+48)|(0))?[ ]?[0-9]{3}[\\- ]?[0-9]{3}[\\- ]?[0-9]{3}$")

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        if(phoneNumber.trim().isEmpty()){
            isPhoneNumberCorrect = false
            userPhoneNumber.postValue(Status.EMPTY)
        }else{
            if(phoneNumberPattern.matcher(phoneNumber).find()){
                isPhoneNumberCorrect = true
                userPhoneNumber.postValue(Status.OK)
            }else{
                isPhoneNumberCorrect = false
                userPhoneNumber.postValue(Status.WRONG_PATTERN)
            }
        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
    }

    fun apartmentNumberValidation(apartmentNumber: String){
        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        val apartmentNumberPattern: Pattern = Pattern.compile("^([0-9]+)([/])([0-9]+\$)")
        val apartmentNumberPattern2: Pattern = Pattern.compile("^([0-9]+\$)")

        if(apartmentNumber.trim().isNotEmpty()){
            if(!apartmentNumberPattern.matcher(apartmentNumber).find()){
                if(apartmentNumberPattern2.matcher(apartmentNumber).find()){
                    isApartmentNumberCorrect = true
                    apartmentNumberStatus.postValue(Status.OK)
                    addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
                    return
                }
                isApartmentNumberCorrect = false
                apartmentNumberStatus.postValue(Status.WRONG_PATTERN)
                return
            }
        }else{
            isApartmentNumberCorrect = false
            apartmentNumberStatus.postValue(Status.EMPTY)
            return
        }

        isApartmentNumberCorrect = true
        apartmentNumberStatus.postValue(Status.OK)

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

    }

    fun apartmentPeopleValidation(number: String){

        isMaxPeopleCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        val numberPattern: Pattern = Pattern.compile("^([0-9]+\$)")

        if(number.trim().isEmpty()){
            isMaxPeopleCorrect = true
            apartmentMaxPeopleStatus.postValue(isMaxPeopleCorrect)
        }else{
            if(numberPattern.matcher(number).find()){
                isMaxPeopleCorrect = true
                apartmentMaxPeopleStatus.postValue(isMaxPeopleCorrect)
            }else{
                isMaxPeopleCorrect = false
                apartmentMaxPeopleStatus.postValue(isMaxPeopleCorrect)
            }

        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
    }

    fun apartmentAreaValidation(number: String){

        isAreaCorrect = false

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)

        val numberPattern: Pattern = Pattern.compile("^([0-9]+\$)")

        if(number.trim().isEmpty()){
            isAreaCorrect = true
            apartmentAreaStatus.postValue(isAreaCorrect)
        }else{
            if(numberPattern.matcher(number).find()){
                isAreaCorrect = true
                apartmentAreaStatus.postValue(isAreaCorrect)
            }else{
                isAreaCorrect = false
                apartmentAreaStatus.postValue(isAreaCorrect)
            }

        }

        addApartmentPossibility.postValue(isNameCorrect && isCityCorrect && isStreetCorrect &&
                isApartmentNumberCorrect && isPhoneNumberCorrect && isAreaCorrect && isMaxPeopleCorrect)
    }

    enum class Status {
        OK, EMPTY, WRONG_PATTERN
    }

    data class LatLng(var latitude: Float, var longitude: Float)
}