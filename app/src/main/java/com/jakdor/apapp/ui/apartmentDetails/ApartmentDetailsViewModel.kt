package com.jakdor.apapp.ui.apartmentDetails

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.common.model.rating.Rating
import com.jakdor.apapp.common.repository.ApartmentRepository
import com.jakdor.apapp.common.repository.RatingRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject

class ApartmentDetailsViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val apartmentRepository: ApartmentRepository,
                    private val ratingRepository: RatingRepository):
    BaseViewModel(application, rxSchedulersFacade)
{
    val ratingListLiveData = MutableLiveData<List<Rating>>()

    fun observeRatingListSubject(){
        disposable.add(ratingRepository.ratingListSubject
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t -> when (t){
                RatingRepository.RatingRepositoryStatusEnum.IDLE -> {}
                RatingRepository.RatingRepositoryStatusEnum.PENDING -> loadingStatus.postValue(true)
                RatingRepository.RatingRepositoryStatusEnum.READY -> {
                    loadingStatus.postValue(false)
                    ratingListLiveData.postValue(ratingRepository.ratingList)
                }
                RatingRepository.RatingRepositoryStatusEnum.ERROR -> loadingStatus.postValue(false)
                else -> {}
            }},{e ->
                run {
                    Timber.e(e, "ERROR observing ApartmentsListSubject")
                    loadingStatus.postValue(false)
                }
            }))
    }

    fun requestNewRatings(apartmentId: Int){
        ratingRepository.requestRating(apartmentId, 50, 0)
    }

    fun getApartment(apartmentId: Int): Apartment?{
        return if(apartmentRepository.apartmentListCache != null){
            apartmentRepository.apartmentListCache!!.find { apartment -> apartment.id == apartmentId }
        } else{
            null
        }
    }

    fun calculateAvgRating(apartmentId: Int): Float{
        val apartment = getApartment(apartmentId) ?: return 0.0f

        var divider = 0.0f

        val sum = apartment.ownerRating * ownerRatingWeight + apartment.locationRating * locationRatingWeight +
                apartment.standardRating * standardRatingWeight + apartment.priceRating * priceRatingWeight
        divider += ownerRatingWeight + locationRatingWeight + standardRatingWeight + priceRatingWeight

        return if(divider == 0.0f) 0.0f else sum/divider
    }

    companion object{
        const val ownerRatingWeight: Float = 1.0f
        const val locationRatingWeight: Float = 1.0f
        const val standardRatingWeight: Float = 1.0f
        const val priceRatingWeight: Float = 1.0f
    }
}