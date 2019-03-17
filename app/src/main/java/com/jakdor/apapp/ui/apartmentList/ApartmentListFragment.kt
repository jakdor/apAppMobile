package com.jakdor.apapp.ui.apartmentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.StackQuestions
import com.jakdor.apapp.databinding.FragmentApartmentListBinding
import com.jakdor.apapp.di.InjectableFragment
import javax.inject.Inject

class ApartmentListFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentListViewModel? = null
    private lateinit var binding: FragmentApartmentListBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apartment_list, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null){
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApartmentListViewModel::class.java)
        }

        binding.viewModel = viewModel
        observeStackQuestionsLiveData()

        viewModel?.observeStackQuestionsSubject()
        viewModel?.requestStackQuestionsUpdate()
    }

    fun observeStackQuestionsLiveData(){
        viewModel?.stackQuestionsLiveData?.observe(this, Observer {
            handleNewStackQuestions(it)
        })
    }

    fun handleNewStackQuestions(model: StackQuestions){
        binding.title = getString(R.string.apartment_list_title_prefix) + ": " + model.quotaRemaining.toString()
    }

    companion object {
        const val CLASS_TAG = "ApartmentListFragment"

        fun getInstance(): ApartmentListFragment {
            val bundle = Bundle()
            val fragment = ApartmentListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}