package com.jakdor.apapp.di

import com.jakdor.apapp.ui.apartment.ApartmentViewModel
import com.jakdor.apapp.ui.apartmentDetails.ApartmentDetailsViewModel
import com.jakdor.apapp.ui.apartmentList.ApartmentListViewModel
import com.jakdor.apapp.ui.login.LoginViewModel
import com.jakdor.apapp.ui.registration.RegistrationViewModel
import com.jakdor.apapp.ui.userPanel.UserPanelViewModel
import dagger.Subcomponent

/**
 * ViewModelFactory Dagger setup interface - App SubComponent
 */
@Subcomponent
interface ViewModelSubComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): ViewModelSubComponent
    }

    fun apartmentListViewModel(): ApartmentListViewModel
    fun loginViewModel() : LoginViewModel
    fun registrationViewModel(): RegistrationViewModel
    fun apartmentViewModel(): ApartmentViewModel
    fun apartmentDetailsViewModel(): ApartmentDetailsViewModel
    fun userPanelViewModel() : UserPanelViewModel
}