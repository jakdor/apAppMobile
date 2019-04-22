package com.jakdor.apapp.ui.apartment

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.add_new_apartment.*
import kotlinx.android.synthetic.main.new_apartment.*
import java.io.File
import javax.inject.Inject


class ApartmentFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentViewModel? = null

    private lateinit var recyclerViewAdapter: ApartmentImageAdapter

    private var photos: ArrayList<Picture> = arrayListOf()

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

        recyclerViewAdapter.setOnItemClickListener(object: ApartmentImageAdapter.RecyclerViewItemClickListener {
            override fun onItemClick(view: View, position: Int) {
                photos.removeAt(position)
                (activity as MainActivity).removeImageFromPosition(position)
                recyclerViewAdapter.notifyDataSetChanged()
                if(photos.size == 0){
                    photosCheckbox.isChecked = false
                }
            }

        })

        val addApartmentObserver = Observer<Boolean> { newStatus ->
            add_apartment_button.isEnabled = newStatus
        }

        viewModel?.addApartmentPossibility?.observe(this, addApartmentObserver)

        observeApartmentIdLiveData()

        viewModel?.observeApartmentIdSubject()

        observeApartmentNameStatus()
        observeApartmentCityStatus()
        observeApartmentStreetStatus()
        observeApartmentNumberStatus()
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
            val name = apartment_name_editText.text.toString()
            val city = apartment_city_editText.text.toString()
            val street = apartment_street_editText.text.toString()
            val apartmentNumber = apartment_number_editText.text.toString()

            val fullAddress = "$street $apartmentNumber, $city"
            val latLng: ApartmentViewModel.LatLng? = viewModel?.getLatLng(activity, fullAddress)
            var lng = 0.0F
            var lat = 0.0F
            if(latLng != null) {
                lng = latLng.longitude
                lat = latLng.latitude
            }

            viewModel?.addApartment(name,city,street,apartmentNumber, lat, lng)
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
            photosCheckbox.isChecked = true
    }

    fun observeApartmentIdLiveData(){
        viewModel?.apartmentIdLiveData?.observe(this, Observer {
            handleNewApartmentId(it)
        })
    }

    fun handleNewApartmentId(apartmentId: Int){
        Toast.makeText(activity,apartmentId.toString(),Toast.LENGTH_SHORT).show()
        if(apartmentId > 0 && photos.size>0){
            viewModel?.addApartmentImage(apartmentId, photos)
        }
        (activity as MainActivity).switchToApartmentListFragment()
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

    private fun handleApartmentStatus(status: Boolean, input: TextInputLayout) {
        if(status){
            input.isErrorEnabled = false
        }else{
            input.error = getString(R.string.noEmptyField)
        }
    }

    private fun handleNewNumberStatus(status: ApartmentViewModel.ApartmentNumberStatus) {
        when(status){
            ApartmentViewModel.ApartmentNumberStatus.OK -> apartment_number_wrapper.isErrorEnabled = false
            ApartmentViewModel.ApartmentNumberStatus.EMPTY ->
                apartment_number_wrapper.error = getString(R.string.noEmptyField)
            ApartmentViewModel.ApartmentNumberStatus.WRONG_PATTERN ->
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