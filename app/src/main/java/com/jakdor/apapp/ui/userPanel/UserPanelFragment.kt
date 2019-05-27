package com.jakdor.apapp.ui.userPanel

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.apartment.Apartment
import com.jakdor.apapp.databinding.FragmentUserPanelBinding
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import com.jakdor.apapp.ui.apartmentList.ApartmentItemAdapter
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_user_panel.*
import java.util.*
import javax.inject.Inject

class UserPanelFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: UserPanelViewModel? = null
    private lateinit var binding: FragmentUserPanelBinding

    private lateinit var recyclerViewAdapter: ApartmentItemAdapter
    private var recyclerViewInit = false
    private var initSubs = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_panel, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerViewInit = false
        swipe_refresh_layout.setOnRefreshListener {
            viewModel?.requestApartmentsListUpdate()
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(UserPanelViewModel::class.java)
        }

        binding.viewModel = viewModel

        if(!initSubs) {
            observeStackQuestionsLiveData()
            viewModel?.observeUserDetailsSubject()
            viewModel?.downloadError?.observe(this, Observer {
                if (it) Toast.makeText(activity, getString(R.string.data_download_error), Toast.LENGTH_LONG).show()
            })
            initSubs = true
        }

        viewModel?.requestApartmentsListUpdate()

        swipe_refresh_layout.isRefreshing = true
    }

    fun observeStackQuestionsLiveData() {
        viewModel?.userDetailsLiveData?.observe(this, Observer {
            showNameTextView.text = it.personalData.firstName
            showSurnameTextView.text = it.personalData.lastName
            apartment_rating_bar.rating = it.user.rate.toFloat()
            showLoginTextView.text = it.user.login
            showEmailTextView.text = it.user.email
            handleNewApartmentList(it.apartments)

        })
    }

    fun handleNewApartmentList(apartments: List<Apartment>?) {
        if (!recyclerViewInit) initRecyclerView()
        if (apartments != null) recyclerViewAdapter.updateItems(apartments.toMutableList())
        item_recycler.scrollToPosition(0)
        swipe_refresh_layout.isRefreshing = false
    }

    fun initRecyclerView() {
        recyclerViewAdapter = ApartmentItemAdapter(Vector(), GlideApp.with(this))
        recyclerViewAdapter.recyclerViewItemClickListener = object : ApartmentItemAdapter.RecyclerViewItemClickListener{
            override fun onItemClick(apartmentId: Int) {
                if(activity is MainActivity) (activity as MainActivity).switchToApartmentDetailsFragment(apartmentId)
            }
        }
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