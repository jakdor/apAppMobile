package com.jakdor.apapp.ui.apartment

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.utils.RxSchedulersFacade
import pl.aprilapps.easyphotopicker.EasyImage
import javax.inject.Inject

class ApartmentViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade):
                    BaseViewModel(application, rxSchedulersFacade){
}