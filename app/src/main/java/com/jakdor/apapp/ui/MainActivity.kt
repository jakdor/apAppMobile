package com.jakdor.apapp.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.jakdor.apapp.R
import com.jakdor.apapp.common.repository.AuthRepository
import com.jakdor.apapp.ui.apartment.ApartmentFragment
import com.jakdor.apapp.ui.apartmentList.ApartmentListFragment
import com.jakdor.apapp.ui.login.LoginFragment
import com.jakdor.apapp.ui.registration.RegistrationFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pl.aprilapps.easyphotopicker.*
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), HasSupportFragmentInjector {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var easyImage: EasyImage

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val externalStorageCheck = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (externalStorageCheck != PackageManager.PERMISSION_GRANTED &&
            cameraCheck != PackageManager.PERMISSION_GRANTED) {
            Nammu.askForPermission(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA), object : PermissionCallback {
                override fun permissionGranted() {

                }

                override fun permissionRefused() {
                    finish()
                }

            })
        }

        easyImage = EasyImage.Builder(this)
            .setChooserTitle("Wybierz")
            .setCopyImagesToPublicGalleryFolder(false)
            .setChooserType(ChooserType.CAMERA_AND_GALLERY)
            .setFolderName("Apartment image")
            .allowMultiple(true)
            .build()

        switchToAddApartmentFragment()
        /*if(authRepository.isLoggedIn()){
            switchToApartmentListFragment()
        }
        else{
            switchToLoginFragment()
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val apartmentFragment = supportFragmentManager.findFragmentByTag(ApartmentFragment.CLASS_TAG) as ApartmentFragment

        easyImage.handleActivityResult(requestCode, resultCode, data, this, object : DefaultCallback() {
            override fun onMediaFilesPicked(imageFiles: Array<MediaFile>, source: MediaSource) {
                apartmentFragment.onPhotosReturned(imageFiles)
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

    fun switchToApartmentListFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentLayout, ApartmentListFragment.getInstance(), ApartmentListFragment.CLASS_TAG)
            .commit()
        Timber.i("Lunched ApartmentListFragment")
    }

    fun switchToLoginFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentLayout, LoginFragment.getInstance(), LoginFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched LoginFragment")
    }

    fun switchToAddApartmentFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentLayout, ApartmentFragment.getInstance(), ApartmentFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched ApartmentFragment")
    }

    fun addRegistrationFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentLayout, RegistrationFragment.getInstance(), RegistrationFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched RegistrationFragment")
    }

    override fun onBackPressed() {
        if(supportFragmentManager.findFragmentByTag(RegistrationFragment.CLASS_TAG) != null){
            switchToLoginFragment()
        }
        else{
            super.onBackPressed()
        }
    }

    fun openChooser(){
        val isCameraAvailable = checkCameraFeaturesAvailability()
        val isGalleryApp = checkGalleryAppAvailability()

        if (isCameraAvailable && isGalleryApp) {
            easyImage.openChooser(this)
        } else if (isGalleryApp) {
            easyImage.openGallery(this)
        }
    }

    fun checkCameraFeaturesAvailability(): Boolean {
        //device has no camera
        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
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
}
