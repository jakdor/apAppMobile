package com.jakdor.apapp.arch

import android.util.ArrayMap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jakdor.apapp.di.ViewModelSubComponent
import com.jakdor.apapp.ui.apartment.ApartmentViewModel
import com.jakdor.apapp.ui.apartmentDetails.ApartmentDetailsViewModel
import com.jakdor.apapp.ui.registration.RegistrationViewModel
import com.jakdor.apapp.ui.apartmentList.ApartmentListViewModel
import com.jakdor.apapp.ui.login.LoginViewModel
import com.jakdor.apapp.ui.userPanel.UserPanelViewModel
import java.util.concurrent.Callable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Factory for ViewModel instances
 */
@Suppress("UNCHECKED_CAST")
@Singleton
class ViewModelFactory
/**
 * ViewModels injected into creators ArrayMap
 * @param viewModelSubComponent Dagger SubComponent ViewModel interface
 */
@Inject
constructor(viewModelSubComponent: ViewModelSubComponent) : ViewModelProvider.Factory {

    private val creators: ArrayMap<Class<*>, Callable<out ViewModel>> = ArrayMap()

    init {
        creators[ApartmentListViewModel::class.java] = Callable { viewModelSubComponent.apartmentListViewModel() }
        creators[LoginViewModel::class.java] = Callable { viewModelSubComponent.loginViewModel() }
        creators[RegistrationViewModel::class.java] = Callable { viewModelSubComponent.registrationViewModel() }
        creators[ApartmentViewModel::class.java] = Callable { viewModelSubComponent.apartmentViewModel() }
        creators[ApartmentDetailsViewModel::class.java] = Callable { viewModelSubComponent.apartmentDetailsViewModel() }
        creators[UserPanelViewModel::class.java] = Callable { viewModelSubComponent.userPanelViewModel() }
    }

    /**
     * creates ViewModels
     * @param modelClass (viewModelChildName).class
     * @param <T> class
     * @return ViewModel custom instance
     */
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator: Callable<out ViewModel>? = creators[modelClass]
        if (creator == null) {
            for ((key, value) in creators) {
                if (modelClass.isAssignableFrom(key)) {
                    creator = value
                    break
                }
            }
        }

        if (creator == null) {
            throw IllegalArgumentException("Model class not found$modelClass")
        }

        try {
            return creator.call() as T
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

    }
}