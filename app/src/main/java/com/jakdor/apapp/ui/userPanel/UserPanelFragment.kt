package com.jakdor.apapp.ui.userPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.databinding.FragmentUserPanelBinding
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.apartmentList.ApartmentItemAdapter
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_registration.*
import kotlinx.android.synthetic.main.fragment_user_panel.*
import java.util.Vector
import javax.inject.Inject

class UserPanelFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: UserPanelViewModel? = null
    private lateinit var binding: FragmentUserPanelBinding

    private lateinit var recyclerViewAdapter: ApartmentItemAdapter
    private var recyclerViewInit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_panel, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null){
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserPanelViewModel::class.java)
        }

        binding.viewModel = viewModel
        observeStackQuestionsLiveData()

        viewModel?.observeApartmentsListSubject()
        viewModel?.requestApartmentsListUpdate()
        swipe_refresh_layout.isRefreshing = true
    }

    override fun onRefresh() {
        viewModel?.requestApartmentsListUpdate()
    }

    fun observeStackQuestionsLiveData(){
        viewModel?.userDetailsLiveData?.observe(this, Observer {
            showNameTextView.text = it.personalData.firstName
            showSurnameTextView.text = it.personalData.lastName
            ratingTextView.text = it.user.rate.toString()
            showLoginTextView.text = it.user.login
            showEmailTextView.text = it.user.email
            handleNewApartmentList(it.apartments)
        })
    }

    fun handleNewApartmentList(apartments : List<Apartment>?){
        if (!recyclerViewInit) initRecyclerView()
        if(apartments != null) recyclerViewAdapter.updateItems(apartments!!.toMutableList())

        item_recycler.scrollToPosition(0)

        swipe_refresh_layout.isRefreshing = false
    }

    fun initRecyclerView(){
        recyclerViewAdapter = ApartmentItemAdapter(Vector(), GlideApp.with(this))
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        item_recycler.layoutManager = linearLayoutManager
        item_recycler.adapter = recyclerViewAdapter
        recyclerViewInit = true
    }

    companion object {
        const val CLASS_TAG = "UserPanel"

        fun getInstance(): UserPanelFragment {
            val bundle = Bundle()
            val fragment = UserPanelFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}