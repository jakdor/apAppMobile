package com.jakdor.apapp.ui.registration

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import javax.inject.Inject

class RegistrationFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: RegistrationViewModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(RegistrationViewModel::class.java)
        }
    }

    companion object {
        const val CLASS_TAG = "RegistrationFragment"

        fun getInstance(): RegistrationFragment {
            val bundle = Bundle()
            val fragment = RegistrationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}
