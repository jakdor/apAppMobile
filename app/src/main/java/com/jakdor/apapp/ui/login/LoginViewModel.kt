package com.jakdor.apapp.ui.login

import android.app.Application
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import javax.inject.Inject

class LoginViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
    BaseViewModel(application, rxSchedulersFacade){


    val loginPossibility = MutableLiveData<Boolean>().apply { postValue(false) }

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

}