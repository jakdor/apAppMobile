package com.jakdor.apapp.di

import com.jakdor.apapp.ui.apartment.ApartmentFragment
import com.jakdor.apapp.ui.apartmentDetails.ApartmentDetailsFragment
import com.jakdor.apapp.ui.apartmentList.ApartmentListFragment
import com.jakdor.apapp.ui.login.LoginFragment
import com.jakdor.apapp.ui.registration.RegistrationFragment
import com.jakdor.apapp.ui.userPanel.UserPanelFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * MainActivity fragment injection point
 */
@Module
abstract class MainActivityMVVMFragmentBuilderModule {
    @ContributesAndroidInjector
    abstract fun contributeApartmentListFragment(): ApartmentListFragment

    @ContributesAndroidInjector
    abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun contributeRegistrationFragment(): RegistrationFragment

    @ContributesAndroidInjector
    abstract fun contributeApartmentFragment(): ApartmentFragment

    @ContributesAndroidInjector
    abstract fun contributeApartmentDetailsFragment(): ApartmentDetailsFragment

    @ContributesAndroidInjector
    abstract fun contributeUserPanelFragment(): UserPanelFragment
}
