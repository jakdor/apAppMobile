package com.jakdor.apapp.ui.apartment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.ui.MainActivity
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.add_new_apartment.*
import javax.inject.Inject


class ApartmentFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentViewModel? = null

    private lateinit var recyclerViewAdapter: ApartmentImageAdapter

    private var photos: ArrayList<Bitmap> = arrayListOf()

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

    fun onPhotosReturned(returnedPhotos: ArrayList<String>) {
            for(image in returnedPhotos){
                photos.add(BitmapFactory.decodeFile(image))
            }
            recyclerViewAdapter.notifyDataSetChanged()
            item_recycler.scrollToPosition(photos.size - 1)
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