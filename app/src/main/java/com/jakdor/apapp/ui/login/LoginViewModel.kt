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


    val loginPossibility = MutableLiveData<Boolean>().apply { value = false }

    var isLoginFilled: Boolean = false
    var isPasswordFilled: Boolean = false

    fun checkLoginFilled (etText : EditText)  {
        isLoginFilled = !etText.text.toString().trim().isEmpty()
        loginPossibility.value = isLoginFilled && isPasswordFilled

    }

    fun checkPasswordFilled (etText : EditText)  {
        isPasswordFilled = !etText.text.toString().trim().isEmpty()
        loginPossibility.value = isLoginFilled && isPasswordFilled
    }

}