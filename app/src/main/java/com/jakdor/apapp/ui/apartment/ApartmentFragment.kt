package com.jakdor.apapp.ui.apartment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.add_new_apartment.*
import kotlinx.android.synthetic.main.image_apartment_list.*
import pl.aprilapps.easyphotopicker.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class ApartmentFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentViewModel? = null

    private lateinit var recyclerViewAdapter: ApartmentImageAdapter

    private val PHOTOS_KEY: String = "apartment_images_list"
    private var photos: ArrayList<MediaFile> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.show()
        }

        if(savedInstanceState != null){
            photos = savedInstanceState.getParcelableArrayList(PHOTOS_KEY)
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
            recyclerViewAdapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(PHOTOS_KEY, photos)
    }

    fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        if(returnedPhotos.size <= 8) {
            photos.addAll(returnedPhotos)
            recyclerViewAdapter.notifyDataSetChanged()
            item_recycler.scrollToPosition(photos.size - 1)
        }else{
            for(image in 0..7){
                photos.add(returnedPhotos[image])
            }
            recyclerViewAdapter.notifyDataSetChanged()
            item_recycler.scrollToPosition(photos.size - 1)
            Toast.makeText(activity,"Można dodać maksymalnie 8 zdjęć", Toast.LENGTH_SHORT).show()
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