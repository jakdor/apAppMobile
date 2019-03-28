package com.jakdor.apapp.di

import com.jakdor.apapp.ui.registration.RegistrationFragment
import com.jakdor.apapp.ui.apartmentList.ApartmentListFragment
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
    abstract fun contributeRegistrationFragment(): RegistrationFragment
}
