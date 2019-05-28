package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.rating.Rating
import com.jakdor.apapp.common.model.rating.RatingListResponse
import com.jakdor.apapp.network.BearerAuthWrapper
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class RatingRepository
@Inject constructor(private val bearerAuthWrapper: BearerAuthWrapper,
                    private val rxSchedulersFacade: RxSchedulersFacade){

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val ratingListSubject: BehaviorSubject<RatingRepositoryStatusEnum> = BehaviorSubject.create()

    var ratingList = mutableListOf<Rating>()

    private var internalLimit = 0
    private var internalOffset = 0

    fun requestRating(apartmentId: Int, limit: Int, offset: Int){
        internalLimit = limit
        internalOffset = offset

        ratingListSubject.onNext(RatingRepositoryStatusEnum.PENDING)

        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.getRatings(apartmentId, limit, offset))
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({t: RatingListResponse? ->
                run {
                    if(t?.ratingsList != null){
                        ratingList.clear()
                        ratingList.addAll(t.ratingsList!!)
                        ratingListSubject.onNext(RatingRepositoryStatusEnum.READY)
                    }
                    else{
                        ratingListSubject.onNext(RatingRepositoryStatusEnum.READY)
                    }
                }
            }, {e ->
                run {
                    Timber.e(e, "ERROR observing getRatings")
                    ratingListSubject.onNext(RatingRepositoryStatusEnum.ERROR)
                }
            }))
    }

    fun dispose(){
        if(!rxDisposables.isDisposed) rxDisposables.dispose()
    }

    enum class RatingRepositoryStatusEnum{
        IDLE, PENDING, READY, ERROR
    }
}