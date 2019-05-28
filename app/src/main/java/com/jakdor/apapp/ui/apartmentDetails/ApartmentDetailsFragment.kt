package com.jakdor.apapp.ui.apartmentDetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.rating.Rating
import com.jakdor.apapp.databinding.FragmentApartmentDetailsBinding
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_apartment_details.*
import java.util.*
import javax.inject.Inject

class ApartmentDetailsFragment : Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentDetailsViewModel? = null
    private lateinit var binding: FragmentApartmentDetailsBinding
    private var apartmentId: Int = -1
    private var phoneNumber: String = ""
    private var getPhoneNumber = true

    private lateinit var recyclerViewAdapter: RatingItemAdapter
    private var recyclerViewInit = false

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        call_button.setOnClickListener {
            if(getPhoneNumber) {
                viewModel?.getApartmentPhoneNumber(apartmentId)
                getPhoneNumber = false
            }

            val builder = AlertDialog.Builder(activity as MainActivity)
            builder.setTitle("Wybierz akcję")
            builder.setItems(arrayOf("Zadzwoń", "Wyślij SMS")) { dialog, which ->
                when(which){
                    0 -> {  val callIntent = Intent(Intent.ACTION_DIAL)
                        callIntent.data = Uri.parse("tel:$phoneNumber")
                        phoneNumber = ""
                        startActivity(callIntent)
                    }
                    1 -> {
                        val smsIntent = Intent(Intent.ACTION_VIEW)
                        smsIntent.type = "vnd.android-dir/mms-sms"
                        smsIntent.putExtra("address", phoneNumber)
                        startActivity(smsIntent)
                    }
                }
                getPhoneNumber = true
            }
            builder.show()
        }

        apartment_map_fab.setOnClickListener {
            val apart = viewModel?.getApartment(apartmentId)
            if(activity is MainActivity && apart != null)
                (activity as MainActivity).openGoogleMaps(apart.lat, apart.long, apart.name)
        }

        apartment_title_bar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if(viewModel == null){
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApartmentDetailsViewModel::class.java)
        }

        if(viewModel != null){
            binding.viewModel = viewModel
            binding.apartment = viewModel!!.getApartment(apartmentId)
        }

        val apartment = viewModel?.getApartment(apartmentId)

        if(apartment != null){
            val adapter = ApartmentImgPagerAdapter(apartment.imgList, GlideApp.with(this), context!!)

            adapter.setOnItemClickListener(object : ApartmentImgPagerAdapter.OnItemClickListener{
                override fun onItemClick(view: View, position: Int) {
                    if(activity is MainActivity && apartment.imgList != null)
                        (activity as MainActivity).switchToImageActivity(apartment.imgList!![position])
                }
            })

            apartment_img_pager.adapter = adapter
            apartment_img_pager_tab_indicator.setupWithViewPager(apartment_img_pager, true)
        }

        viewModel?.observeApartmentPhoneNumber()

        viewModel?.ratingListLiveData?.observe(this, Observer { handleNewRatingList(it) })
        viewModel?.observeRatingListSubject()
        viewModel?.requestNewRatings(apartmentId)

        viewModel?.apartPhoneNumberLiveData?.observe(this, Observer { handleNewApartmentPhoneNumber(it) })
        viewModel?.observeApartmentPhoneNumber()
        viewModel?.getApartmentPhoneNumber(apartmentId)

        apartment_rating_bar.rating = viewModel?.calculateAvgRating(apartmentId) ?: 0.0f
    }

    private fun handleNewRatingList(ratings: List<Rating>){
        if (!recyclerViewInit) initRecyclerView()
        recyclerViewAdapter.updateItems(ratings.toMutableList())
    }

    private fun handleNewApartmentPhoneNumber(tempPhoneNumber: String?){
        if(tempPhoneNumber != null && tempPhoneNumber.trim().isNotEmpty()){
            this.phoneNumber = tempPhoneNumber
            getPhoneNumber = true
        }else if(tempPhoneNumber!!.trim().isEmpty()){
            Toast.makeText(activity,"Brak numeru telefonu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initRecyclerView(){
        recyclerViewAdapter = RatingItemAdapter(Vector())
        recyclerViewAdapter.recyclerViewItemClickListener = object : RatingItemAdapter.RecyclerViewItemClickListener{
            override fun onItemClick(ratingId: Int) {

            }
        }
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        binding.apartmentRatingRecycler.layoutManager = linearLayoutManager
        binding.apartmentRatingRecycler.adapter = recyclerViewAdapter
        recyclerViewInit = true
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