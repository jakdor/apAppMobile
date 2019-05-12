package com.jakdor.apapp.ui.userPanel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.userDetails.UserDetails
import com.jakdor.apapp.common.repository.AuthRepository
import com.jakdor.apapp.common.repository.UserDetailsRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject
import com.auth0.android.jwt.JWT


class UserPanelViewModel
@Inject constructor(
    application: Application,
    rxSchedulersFacade: RxSchedulersFacade,
    private val userDetailsRepository: UserDetailsRepository,
    authRepository: AuthRepository
) :
    BaseViewModel(application, rxSchedulersFacade) {


    private val parsedJWT = JWT(authRepository.getBearerToken())
    private val subscriptionMetaData = parsedJWT.getClaim("sub")
    private val userID = subscriptionMetaData.asString()

    val userDetailsLiveData = MutableLiveData<UserDetails>()

    fun observeApartmentsListSubject() {
        disposable.add(
            userDetailsRepository.userDetailsSubject
                .observeOn(rxSchedulersFacade.io())
                .subscribeOn(rxSchedulersFacade.io())
                .subscribe({ t: UserDetails -> userDetailsLiveData.postValue(t) },
                    { e -> Timber.e(e, "ERROR observing UserDetailsSubject") })
        )
    }

    fun requestApartmentsListUpdate() {


        userDetailsRepository.requestUserDetails(userID)
    }
}