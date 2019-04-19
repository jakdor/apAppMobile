package com.jakdor.apapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.fxn.utility.ImageQuality
import com.jakdor.apapp.R
import com.jakdor.apapp.common.repository.AuthRepository
import com.jakdor.apapp.ui.apartment.ApartmentFragment
import com.jakdor.apapp.ui.apartmentList.ApartmentListFragment
import com.jakdor.apapp.ui.login.LoginFragment
import com.jakdor.apapp.ui.registration.RegistrationFragment
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import pl.tajchert.nammu.Nammu
import pl.tajchert.nammu.PermissionCallback
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector{

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var options: Options
    private var returnedImages: ArrayList<String> = arrayListOf()

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

            options = Options.init()
                .setRequestCode(100)
                .setCount(8)
                .setPreSelectedUrls(returnedImages)
                .setPath("/")

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

        if (resultCode === Activity.RESULT_OK && requestCode === 100) {
            returnedImages.clear()
            returnedImages.addAll(data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS))
            apartmentFragment.onPhotosReturned(returnedImages)
        }
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

        if (isCameraAvailable) {
            Pix.start(this,options)
        }
    }

    fun checkCameraFeaturesAvailability(): Boolean {
        //device has no camera
        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true
        }
        return false
    }
}
