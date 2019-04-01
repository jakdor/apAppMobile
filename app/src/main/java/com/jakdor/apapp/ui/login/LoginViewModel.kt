package com.jakdor.apapp.ui.login

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.repository.AuthRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val authRepository: AuthRepository):
    BaseViewModel(application, rxSchedulersFacade){

    val loginPossibility = MutableLiveData<Boolean>().apply { postValue(false) }
    val loginRequestStatus = MutableLiveData<Boolean>()

    var isLoginFilled: Boolean = false
    var isPasswordFilled: Boolean = false

    fun checkLoginFilled (etText : String)  {
        isLoginFilled = !etText.trim().isEmpty()
        loginPossibility.postValue(isLoginFilled && isPasswordFilled)
    }

    fun checkPasswordFilled (etText : String)  {
        isPasswordFilled = !etText.trim().isEmpty()
        loginPossibility.postValue( isLoginFilled && isPasswordFilled)
    }

    fun login(login: String, password: String){
        disposable.add(authRepository.login(login, password)
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({t -> loginRequestStatus.postValue(t)},
                {e -> Timber.e(e, "ERROR observing loginRequest")}))
    }

}