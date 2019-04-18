package com.jakdor.apapp.ui.apartment

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakdor.apapp.R
import com.jakdor.apapp.di.InjectableFragment
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.add_new_apartment.*
import pl.aprilapps.easyphotopicker.*
import java.util.*
import javax.inject.Inject


class ApartmentFragment: Fragment(), InjectableFragment {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    var viewModel: ApartmentViewModel? = null

    private lateinit var recyclerViewAdapter: ApartmentImageAdapter
    private lateinit var easyImage: EasyImage

    private val PHOTOS_KEY: String = "apartment_images_list"
    private var photos: ArrayList<MediaFile>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (activity != null && activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar!!.show()
        }

        if(savedInstanceState != null){
            photos = savedInstanceState.getParcelableArrayList(PHOTOS_KEY)
        }

        if(activity?.applicationContext != null) {
            easyImage = EasyImage.Builder(activity!!.applicationContext)
                .setChooserTitle("Wybierz")
                .setCopyImagesToPublicGalleryFolder(false)
                .setChooserType(ChooserType.CAMERA_AND_GALLERY)
                .setFolderName("Apartment image")
                .allowMultiple(true)
                .build()
        }

        return inflater.inflate(R.layout.add_new_apartment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ApartmentViewModel::class.java)
        }

        recyclerViewAdapter = ApartmentImageAdapter(GlideApp.with(this), Vector())
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.orientation = RecyclerView.HORIZONTAL
        item_recycler.layoutManager = linearLayoutManager
        item_recycler.adapter = recyclerViewAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val isCameraAvailable: Boolean = checkCameraFeaturesAvailability()
        val isGalleryApp: Boolean = checkGalleryAppAvailability()

        add_image_button.setOnClickListener {

            if( isCameraAvailable && isGalleryApp ){
                easyImage.openChooser(this)
            }else if(isGalleryApp){
                easyImage.openGallery(this)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        easyImage.handleActivityResult(requestCode, resultCode, data, activity!!.parent, object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                for(image: MediaFile in imageFiles){
                    onPhotosReturned(imageFiles)
                }
            }

            override fun onImagePickerError(@NonNull error: Throwable, @NonNull source: MediaSource) {
                //Some error handling
                error.printStackTrace()
            }

            override fun onCanceled(@NonNull source: MediaSource) {
                //Not necessary to remove any files manually anymore
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putSerializable(PHOTOS_KEY, photos)
    }

    fun checkCameraFeaturesAvailability(): Boolean {
        //device has no camera
        if (activity?.applicationContext?.packageManager!!.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true
        }
        return false
    }

    fun checkGalleryAppAvailability(): Boolean {
        //device has no app that handles gallery intent
        if (easyImage.canDeviceHandleGallery()) {
            return true
        }
        return false
    }

    fun onPhotosReturned(returnedPhotos: Array<MediaFile>) {
        photos?.addAll(returnedPhotos)
        recyclerViewAdapter.notifyDataSetChanged()
        item_recycler.scrollToPosition(photos?.size!!.minus(1))
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