package com.jakdor.apapp.di

import com.jakdor.apapp.ui.apartmentList.ApartmentListViewModel
import com.jakdor.apapp.ui.login.LoginViewModel
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
}