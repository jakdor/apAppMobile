package com.jakdor.apapp.ui.apartment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import javax.inject.Inject

class ApartmentFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentViewModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.show()
        }
        return inflater.inflate(R.layout.add_new_apartment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApartmentViewModel::class.java)
        }

    }

    companion object {
        const val CLASS_TAG = "ApartmentFragment"

        fun getInstance(): ApartmentFragment {
            val bundle = Bundle()
            val fragment = ApartmentFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}