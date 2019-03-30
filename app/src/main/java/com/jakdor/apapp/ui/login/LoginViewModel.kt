package com.jakdor.apapp.ui.login

import android.app.Application
import android.widget.EditText
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import javax.inject.Inject

class LoginViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
    BaseViewModel(application, rxSchedulersFacade){

    fun isEditTextEmpty (etText : EditText) : Boolean {
        return etText.text.toString().trim().isEmpty()
    }

}