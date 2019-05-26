package com.jakdor.apapp.ui.apartmentDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.jakdor.apapp.R
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.fragment_img.*

/**
 * Fragment displaying image from url provided in newInstance()
 * - No ViewModel required
 */
class ImageFragment : Fragment() {

    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments != null){
            imageUrl = arguments?.getString(IMAGE_URL_BUNDLE_TAG, "") ?: ""
            if(!::imageUrl.isInitialized || imageUrl.isEmpty()) throw Exception("IMAGE_URL_BUNDLE_TAG required")
        }
        else{
            throw Exception("IMAGE_URL_BUNDLE_TAG required")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.fragment_img, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        GlideApp.with(this)
            .load(imageUrl)
            .fitCenter()
            .transition(withCrossFade())
            .into(image_big)
    }

    companion object {
        const val CLASS_TAG = "ImageFragment"
        private const val IMAGE_URL_BUNDLE_TAG = "IMAGE_URL_BUNDLE_TAG"

        fun getInstance(imgUrl: String) : ImageFragment {
            val imageFragment = ImageFragment()
            val bundle = Bundle()
            bundle.putString(IMAGE_URL_BUNDLE_TAG, imgUrl)
            imageFragment.arguments = bundle
            return imageFragment
        }
    }
}