package com.jakdor.apapp.ui.apartment

import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.jakdor.apapp.R
import com.jakdor.apapp.common.model.auth.ApartmentAddResponse
import com.jakdor.apapp.common.model.auth.ApartmentAddStatusEnum
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.add_new_apartment.*
import kotlinx.android.synthetic.main.new_apartment.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ApartmentFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentViewModel? = null

    private lateinit var recyclerViewAdapter: ApartmentImageAdapter

    private var photos: ArrayList<Picture> = arrayListOf()
    private var sentImagesCount = 0

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

        recyclerViewAdapter = ApartmentImageAdapter(GlideApp.with(this),photos)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false)
        item_recycler.layoutManager = linearLayoutManager
        item_recycler.adapter = recyclerViewAdapter

        recyclerViewAdapter.setRecyclerViewItemClickListener(object: ApartmentImageAdapter.RecyclerViewItemClickListener {
            override fun changeThumbnail(position: Int) {
                Collections.swap(photos,position,0)
                (activity as MainActivity).changeThumbnail(position)
                recyclerViewAdapter.notifyDataSetChanged()
            }

            override fun onItemClick(view: View, position: Int) {
                photos.removeAt(position)
                (activity as MainActivity).removeImageFromPosition(position)
                recyclerViewAdapter.notifyDataSetChanged()
                if(photos.size == 0){
                    photosCheckbox.isChecked = false
                }
            }
        })

        viewModel?.getUserPhoneNumber()

        val addApartmentObserver = Observer<Boolean> { newStatus ->
            add_apartment_button.isEnabled = newStatus
        }

        val userPhoneNumberObserver = Observer<String> { phoneNumber ->
            if(phoneNumber!= null && phoneNumber.trim().isNotEmpty()){
                user_phoneNumber_editText.setText(phoneNumber)
                user_phoneNumber_editText.isEnabled = false
            }
        }

        viewModel?.userPhoneNumberLiveData?.observe(this, userPhoneNumberObserver)

        viewModel?.addApartmentPossibility?.observe(this, addApartmentObserver)

        observeApartmentIdLiveData()
        observeSentImagesLiveData()

        viewModel?.observeApartmentIdSubject()
        viewModel?.observeSendingImages()

        observeApartmentNameStatus()
        observeApartmentCityStatus()
        observeApartmentStreetStatus()
        observeApartmentNumberStatus()
        observeUserPhoneNumberStatus()
        observeApartmentAreaStatus()
        observeMaxPeopleStatus()
        observeApartmentPriceStatus()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        add_image_button.setOnClickListener {
            if (activity != null && activity is MainActivity) {
                (activity as MainActivity).openChooser()
            }
        }
        delete_apartment_images.setOnClickListener {
            photos.clear()
            if (activity != null && activity is MainActivity) {
                (activity as MainActivity).clearImages()
            }
            recyclerViewAdapter.notifyDataSetChanged()
            photosCheckbox.isChecked = false
        }
        add_apartment_button.setOnClickListener {
            val name = apartment_name_editText.text.toString().trim()
            val city = apartment_city_editText.text.toString().trim()
            val street = apartment_street_editText.text.toString().trim()
            val apartmentNumber = apartment_number_editText.text.toString()
            val tempPrice = apartment_price_editText.text.toString().trim().toFloat()
            val tempMaxPeople = maxPeople_editText.text.toString().trim()
            var maxPeople = 0
            if(tempMaxPeople.isNotEmpty()){
                maxPeople = tempMaxPeople.toInt()
            }
            val tempArea = apartment_area_editText.text.toString().trim()
            var area = 0
            if(tempArea.isNotEmpty()){
                area = tempArea.toInt()
            }
            val phoneNumber = user_phoneNumber_editText.text.toString().trim()

            val fullAddress = "$street $apartmentNumber, $city"
            val latLng: ApartmentViewModel.LatLng? = viewModel?.getLatLng(activity, fullAddress)
            var lng = 0.0F
            var lat = 0.0F
            if(latLng != null) {
                lng = latLng.longitude
                lat = latLng.latitude
            }

            val price = (tempPrice*100).toInt()

            viewModel?.addApartment(name, city, street, apartmentNumber, price, maxPeople, area, phoneNumber, lat, lng)
        }

        apartment_name_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val apartmentName = apartment_name_editText.text.toString()

                viewModel?.apartmentNameValidation(apartmentName)
            }

        })

        apartment_city_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val apartmentCity = apartment_city_editText.text.toString()

                viewModel?.apartmentCityValidation(apartmentCity)
            }

        })

        apartment_street_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val apartmentStreet = apartment_street_editText.text.toString()

                viewModel?.apartmentStreetValidation(apartmentStreet)
            }

        })

        apartment_number_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val apartmentNumber = apartment_number_editText.text.toString()

                viewModel?.apartmentNumberValidation(apartmentNumber)
            }

        })

        user_phoneNumber_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val phoneNumber = user_phoneNumber_editText.text.toString()

                viewModel?.userPhoneNumberValidation(phoneNumber)
            }

        })

        apartment_area_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val apartmentArea = apartment_area_editText.text.toString()

                viewModel?.apartmentAreaValidation(apartmentArea)
            }

        })

        maxPeople_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val maxPeople = maxPeople_editText.text.toString()

                viewModel?.apartmentPeopleValidation(maxPeople)
            }

        })

        apartment_price_editText.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val apartmentPrice = apartment_price_editText.text.toString()

                viewModel?.apartmentPriceValidation(apartmentPrice)
            }

        })
    }

    fun onPhotosReturned(returnedPhotos: ArrayList<String>) {
        if(photos.size > 0) {
            val newPhotoList: ArrayList<Picture> = arrayListOf()
            for (image in returnedPhotos) {
                newPhotoList.add(Picture(image, BitmapFactory.decodeFile(image)))
            }
            recyclerViewAdapter.updateItems(newPhotoList)
        }else{
            for (image in returnedPhotos) {
                photos.add(Picture(image, BitmapFactory.decodeFile(image)))
            }
        }
        item_recycler.scrollToPosition(photos.size - 1)
        photosCheckbox.isChecked = returnedPhotos.size > 0
    }

    fun observeApartmentIdLiveData(){
        viewModel?.apartmentIdLiveData?.observe(this, Observer {
            handleNewApartmentId(it)
        })
    }

    fun handleNewApartmentId(apartmentStatus: ApartmentAddResponse){
        if(apartmentStatus.apartmentAddStatus == ApartmentAddStatusEnum.OK && photos.size>0){
            add_apartment_button.visibility = View.GONE
            progress_vertical.visibility = View.VISIBLE
            progress_textView.text = (sentImagesCount.toString() + "/" + photos.size.toString())
            viewModel?.addApartmentImage(apartmentStatus.id, photos)
        }else if(photos.size == 0){
            (activity as MainActivity).switchToApartmentListFragment()
        }
        else if(apartmentStatus.apartmentAddStatus == ApartmentAddStatusEnum.ERROR){
            Toast.makeText(activity, getString(R.string.error_adding_apartment), Toast.LENGTH_LONG).show()
        }else if(apartmentStatus.apartmentAddStatus == ApartmentAddStatusEnum.APARTMENT_EXISTS){
            Toast.makeText(activity, getString(R.string.apartment_exists_info), Toast.LENGTH_LONG).show()
        }
    }

    private fun observeSentImagesLiveData() {
        viewModel?.sentImagesLiveData?.observe(this, Observer {
            handleSendingImagesResponse(it)
        })
    }

    private fun handleSendingImagesResponse(sent: Boolean) {
        if(sent){
            uploading_images.max = photos.size
            sentImagesCount++
            uploading_images.progress = sentImagesCount
            progress_textView.text = (sentImagesCount.toString() + "/" + photos.size.toString())

            if(sentImagesCount==photos.size) {
                sentImagesCount = 0
                uploading_images.progress = 0
                (activity as MainActivity).clearImages()
                (activity as MainActivity).switchToApartmentListFragment()
            }
        }
    }

    private fun observeApartmentNameStatus() {
        viewModel?.apartmentNameStatus?.observe(this, Observer {
            handleApartmentStatus(it, apartment_name_wrapper)
        })
    }

    private fun observeApartmentCityStatus(){
        viewModel?.apartmentCityStatus?.observe(this, Observer {
            handleApartmentStatus(it, apartment_city_wrapper)
        })
    }

    private fun observeApartmentStreetStatus() {
        viewModel?.apartmentStreetStatus?.observe(this, Observer {
            handleApartmentStatus(it, apartment_street_wrapper)
        })
    }

    private fun observeApartmentNumberStatus() {
        viewModel?.apartmentNumberStatus?.observe(this, Observer {
            handleNewNumberStatus(it)
        })
    }

    private fun observeUserPhoneNumberStatus() {
        viewModel?.userPhoneNumber?.observe(this, Observer {
            handleNewPhoneNumber(it)
        })
    }

    private fun observeMaxPeopleStatus() {
        viewModel?.apartmentMaxPeopleStatus?.observe(this, Observer {
            handleNewInt(it, maxPeople_wrapper)
        })
    }

    private fun observeApartmentAreaStatus() {
        viewModel?.apartmentAreaStatus?.observe(this, Observer {
            handleNewInt(it, apartment_area_wrapper)
        })
    }

    private fun observeApartmentPriceStatus() {
        viewModel?.apartmentPriceStatus?.observe(this, Observer {
            handleApartmentStatus(it, apartment_price_wrapper)
        })
    }

    private fun handleNewInt(status: Boolean, input: TextInputLayout){
        if(status){
            input.isErrorEnabled = false
        }else{
            input.error = getString(R.string.wrong_number_label)
        }
    }

    private fun handleNewPhoneNumber(status: ApartmentViewModel.Status) {
        when(status){
            ApartmentViewModel.Status.OK -> user_phoneNumber_wrapper.isErrorEnabled = false
            ApartmentViewModel.Status.EMPTY ->
                user_phoneNumber_wrapper.error = getString(R.string.noEmptyField)
            ApartmentViewModel.Status.WRONG_PATTERN ->
                user_phoneNumber_wrapper.error = getString(R.string.wrong_phone_number)
        }
    }

    private fun handleApartmentStatus(status: Boolean, input: TextInputLayout) {
        if(status){
            input.isErrorEnabled = false
        }else{
            input.error = getString(R.string.noEmptyField)
        }
    }

    private fun handleNewNumberStatus(status: ApartmentViewModel.Status) {
        when(status){
            ApartmentViewModel.Status.OK -> apartment_number_wrapper.isErrorEnabled = false
            ApartmentViewModel.Status.EMPTY ->
                apartment_number_wrapper.error = getString(R.string.noEmptyField)
            ApartmentViewModel.Status.WRONG_PATTERN ->
                apartment_number_wrapper.error = getString(R.string.apartmentNumberPatternInfo)
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