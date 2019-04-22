package com.jakdor.apapp.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.google.android.material.snackbar.Snackbar
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
import java.io.File
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

        Nammu.init(this)

            options = Options.init()
                .setRequestCode(100)
                .setCount(8)
                .setPreSelectedUrls(returnedImages)
                .setPath("/DCIM/TrueHome")

        //switchToAddApartmentFragment()
        if(authRepository.isLoggedIn()){
            switchToAddApartmentFragment()
        }
        else{
            switchToLoginFragment()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val apartmentFragment = supportFragmentManager.findFragmentByTag(ApartmentFragment.CLASS_TAG) as ApartmentFragment

        if (resultCode === Activity.RESULT_OK && requestCode === 100) {
            val imagesList = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            if(returnedImages.size > 0) {
                if(returnedImages.size < 8) {
                    for (image in imagesList) {
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(image))))
                        if (!returnedImages.contains(image)) {
                            returnedImages.add(image)
                        }
                    }
                }else{
                    Toast.makeText(this, getString(R.string.selection_limiter_pix, 8), Toast.LENGTH_SHORT).show()
                }
            }else{
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(imagesList[0]))))
                returnedImages.addAll(imagesList)
            }

            apartmentFragment.onPhotosReturned(returnedImages)
        }
        if(resultCode == Activity.RESULT_CANCELED){
            returnedImages.clear()
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
        //val isCameraAvailable = checkCameraFeaturesAvailability()

        val externalStorageCheck = Nammu.checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraCheck = Nammu.checkPermission(Manifest.permission.CAMERA)

        if (!externalStorageCheck || !cameraCheck) {
                Nammu.askForPermission(this, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), object : PermissionCallback {
                    override fun permissionGranted() {
                        Pix.start(this@MainActivity, options)
                    }

                    override fun permissionRefused() {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.camera_externalStorage_refused), Toast.LENGTH_LONG
                        ).show()
                    }

                })
        }else{
            Pix.start(this,options)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(permissions.contains(Manifest.permission.CAMERA) ||
            permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /*fun checkCameraFeaturesAvailability(): Boolean {
        //device has no camera
        if (this.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            return true
        }
        return false
    }*/

    fun clearImages() {
        returnedImages.clear()
    }

    fun removeImageFromPosition(position: Int){
        returnedImages.removeAt(position)
    }
}
