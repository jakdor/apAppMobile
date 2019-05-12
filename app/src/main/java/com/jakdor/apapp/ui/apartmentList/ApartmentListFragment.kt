package com.jakdor.apapp.ui.apartmentList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.apartment.ApartmentList
import com.jakdor.apapp.databinding.FragmentApartmentListBinding
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_apartment_list.*
import java.util.Vector
import javax.inject.Inject

class ApartmentListFragment: Fragment(), SwipeRefreshLayout.OnRefreshListener, InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentListViewModel? = null
    private lateinit var binding: FragmentApartmentListBinding

    private lateinit var recyclerViewAdapter: ApartmentItemAdapter
    private var recyclerViewInit = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if(activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.show()
        }
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_apartment_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh_layout.setOnRefreshListener(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null){
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApartmentListViewModel::class.java)
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
        viewModel?.apartmentsListLiveData?.observe(this, Observer {
            handleNewApartmentList(it)
        })
    }

    fun handleNewApartmentList(model: ApartmentList){
        if (!recyclerViewInit) initRecyclerView()
        if(model.apartments != null) recyclerViewAdapter.updateItems(model.apartments!!.toMutableList())

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
        const val CLASS_TAG = "ApartmentListFragment"

        fun getInstance(): ApartmentListFragment {
            val bundle = Bundle()
            val fragment = ApartmentListFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}