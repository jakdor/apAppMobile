package com.jakdor.apapp.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jakdor.apapp.R
import com.jakdor.apapp.utils.GlideApp
import kotlinx.android.synthetic.main.activity_img.*

class ImageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_img)

        val imageUrl = intent?.extras?.getString(IMAGE_URL_BUNDLE_KEY, "") ?: ""

        GlideApp.with(this)
            .load(imageUrl)
            .fitCenter()
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Drawable>{
                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                             dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    image_progress.visibility = View.GONE
                    return false
                }

                override fun onLoadFailed(
                    e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    finish()
                    return false
                }
            })
            .into(image_big)
    }

    companion object{
        const val IMAGE_URL_BUNDLE_KEY = "IMAGE_URL_BUNDLE_KEY"
    }
}