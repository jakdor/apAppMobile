package com.jakdor.apapp.ui.registration

import android.app.Application
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import javax.inject.Inject

class RegistrationViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
    BaseViewModel(application, rxSchedulersFacade){

    fun validation(){

    }
}
