package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.userDetails.UserPhoneNumberResponse
import com.jakdor.apapp.common.model.userDetails.UserDetails
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.BearerAuthWrapper
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class UserDetailsRepository
@Inject constructor(
    retrofitFactory: RetrofitFactory,
    private val bearerAuthWrapper: BearerAuthWrapper,
    private val rxSchedulersFacade: RxSchedulersFacade
) {

    private val apiService: BackendService =
        retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val userDetailsSubject: BehaviorSubject<UserDetails> = BehaviorSubject.create()

    val userPhoneNumber: BehaviorSubject<String> = BehaviorSubject.create()

    var error: Boolean = false

    fun requestUserDetails() {
        rxDisposables.add(
            bearerAuthWrapper.wrapCall(
                bearerAuthWrapper.apiAuthService.getUserDetails()
            )
                .observeOn(rxSchedulersFacade.io())
                .subscribeOn(rxSchedulersFacade.io())
                .subscribe({ t: UserDetails? ->
                    if (t != null) {
                        userDetailsSubject.onNext(t); error = false
                    }
                },
                    { e -> Timber.e(e, "ERROR observing userDetails"); error = true })
        )
    }

    fun getUserPhoneNumber(){
        rxDisposables.add(bearerAuthWrapper.wrapCall(
            bearerAuthWrapper.apiAuthService.getUserPhoneNumber())
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe ({ t: UserPhoneNumberResponse -> userPhoneNumber.onNext(t.userPhoneNumber)},
                {e -> Timber.e(e,"ERROR getting user phone number")})
        )
    }

    fun dispose() {
        if (!rxDisposables.isDisposed) rxDisposables.dispose()
    }
}