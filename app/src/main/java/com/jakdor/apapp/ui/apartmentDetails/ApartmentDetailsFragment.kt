package com.jakdor.apapp.ui.apartmentDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakdor.apapp.R
import com.jakdor.apapp.databinding.FragmentApartmentDetailsBinding
import com.jakdor.apapp.di.InjectableFragment
import javax.inject.Inject

class ApartmentDetailsFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentDetailsViewModel? = null
    private lateinit var binding: FragmentApartmentDetailsBinding
    private var apartmentId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(arguments != null){
            apartmentId = arguments?.getInt(APARTMENT_ID_BUNDLE_TAG, -1) ?: -1
            if(apartmentId == -1) throw Exception("APARTMENT_ID_BUNDLE_TAG required")
        }
        else{
            throw Exception("APARTMENT_ID_BUNDLE_TAG required")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.show()
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apartment_details, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null){
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApartmentDetailsViewModel::class.java)
        }

        binding.viewModel = viewModel
    }

    companion object {
        const val CLASS_TAG = "ApartmentDetailsFragment"
        private const val APARTMENT_ID_BUNDLE_TAG = "APARTMENT_ID_BUNDLE_TAG"

        fun getInstance(apartmentId: Int): ApartmentDetailsFragment {
            val bundle = Bundle()
            bundle.putInt(APARTMENT_ID_BUNDLE_TAG, apartmentId)
            val fragment = ApartmentDetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}