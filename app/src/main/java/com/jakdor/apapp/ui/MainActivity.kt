package com.jakdor.apapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.jakdor.apapp.R
import com.jakdor.apapp.ui.apartmentList.ApartmentListFragment
import com.jakdor.apapp.ui.login.LoginFragment
import com.jakdor.apapp.ui.registration.RegistrationFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchToLoginFragment();
        //switchToApartmentListFragment()
        //switchToRegistrationFragment()
    }

    fun switchToApartmentListFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentLayout, ApartmentListFragment.getInstance(), ApartmentListFragment.CLASS_TAG)
            .commit()
        Timber.i("Lunched ApartmentListFragment")
    }

    fun switchToLoginFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentLayout, LoginFragment.getInstance(), LoginFragment.CLASS_TAG)
            .commit()
        Timber.i("Lunched LoginFragment")
    }

    fun switchToRegistrationFragment(){
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentLayout, RegistrationFragment.getInstance(), RegistrationFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched RegistrationFragment")
    }
}
