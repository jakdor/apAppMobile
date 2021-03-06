package com.jakdor.apapp.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.jakdor.apapp.R
import com.jakdor.apapp.common.repository.AuthRepository
import com.jakdor.apapp.ui.apartment.ApartmentFragment
import com.jakdor.apapp.ui.apartmentDetails.ApartmentDetailsFragment
import com.jakdor.apapp.ui.apartmentList.ApartmentListFragment
import com.jakdor.apapp.ui.login.LoginFragment
import com.jakdor.apapp.ui.registration.RegistrationFragment
import com.jakdor.apapp.ui.userPanel.UserPanelFragment
import com.jakdor.apapp.utils.RxSchedulersFacade
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.io.File
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), HasSupportFragmentInjector{

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var rxSchedulersFacade: RxSchedulersFacade

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    private lateinit var options: Options
    private var returnedImages: ArrayList<String> = arrayListOf()

    private val rxDisposables = CompositeDisposable()

    override fun supportFragmentInjector(): DispatchingAndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        options = Options.init()
            .setRequestCode(GET_IMAGES_REQUEST_CODE)
            .setCount(MAX_IMAGES_TO_UPLOAD)
            .setPreSelectedUrls(returnedImages)

        if(authRepository.isLoggedIn()){
            switchToApartmentListFragment()
        }
        else{
            switchToLoginFragment()
        }

        rxDisposables.add(authRepository.authStatusSubject
            .observeOn(rxSchedulersFacade.ui())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({t -> onNewAuthStatus(t)}, {Timber.e("Error observing auth status")}))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val apartmentFragment = supportFragmentManager.findFragmentByTag(ApartmentFragment.CLASS_TAG) as ApartmentFragment?

        if (resultCode == Activity.RESULT_OK && requestCode == GET_IMAGES_REQUEST_CODE && apartmentFragment != null) {
            val imagesList = data!!.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            if(returnedImages.size > 0) {
                if(returnedImages.size < MAX_IMAGES_TO_UPLOAD) {
                    for (image in imagesList) {
                        sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(image))))
                        if (!returnedImages.contains(image)) {
                            returnedImages.add(image)
                        }
                    }
                }else{
                    Toast.makeText(this, getString(R.string.selection_limiter_pix, MAX_IMAGES_TO_UPLOAD), Toast.LENGTH_SHORT).show()
                }
            }else{
                sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(imagesList[0]))))
                returnedImages.addAll(imagesList)
            }

            apartmentFragment.onPhotosReturned(returnedImages)
        }
        if(resultCode == Activity.RESULT_CANCELED){
            returnedImages.clear()
            apartmentFragment?.onPhotosReturned(returnedImages)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item != null){
            when(item.itemId){
                R.id.action_add_apartment -> switchToAddApartmentFragment()
                R.id.action_user_details -> switchToUserPanelFragment()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        if(!rxDisposables.isDisposed) rxDisposables.dispose()
    }

    private fun onNewAuthStatus(status: AuthRepository.AuthStatusEnum){
        when(status){
            AuthRepository.AuthStatusEnum.IDLE -> {}
            AuthRepository.AuthStatusEnum.LOGGED_IN -> {}
            AuthRepository.AuthStatusEnum.NO_INTERNET -> {
                Toast.makeText(this, getString(R.string.no_internet_toast), Toast.LENGTH_SHORT).show()
            }
            AuthRepository.AuthStatusEnum.REQUEST_FAILED -> {
                Toast.makeText(this, getString(R.string.no_api_response_toast), Toast.LENGTH_SHORT).show()
            }
            AuthRepository.AuthStatusEnum.LOGOUT_USER -> logout()
            AuthRepository.AuthStatusEnum.LOGOUT_FORCED -> {
                logout()
                Toast.makeText(this, getString(R.string.session_expired_toast), Toast.LENGTH_LONG).show()
            }

        }
    }

    private fun logout(){
        val fragments = supportFragmentManager.fragments.count()
        for (i in 0 .. fragments){
            supportFragmentManager.popBackStack()
        }

        switchToLoginFragment()
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
            .addToBackStack(LoginFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched LoginFragment")
    }

    fun switchToRegistrationFragment(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainFragmentLayout, RegistrationFragment.getInstance(), RegistrationFragment.CLASS_TAG)
            .addToBackStack(RegistrationFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched RegistrationFragment")
    }

    fun switchToApartmentDetailsFragment(apartmentId: Int){
        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentLayout, ApartmentDetailsFragment.getInstance(apartmentId),
                ApartmentDetailsFragment.CLASS_TAG)
            .addToBackStack(ApartmentDetailsFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched ApartmentDetailsFragment")
    }

    private fun switchToAddApartmentFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(ApartmentFragment.CLASS_TAG) as ApartmentFragment?
        if(fragment != null && fragment.isVisible) return

        popFragmentsRecursively()

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentLayout, ApartmentFragment.getInstance(), ApartmentFragment.CLASS_TAG)
            .addToBackStack(ApartmentFragment.CLASS_TAG)
            .commit()
        Timber.i("Launched ApartmentFragment")
    }

    private fun switchToUserPanelFragment() {
        val fragment = supportFragmentManager.findFragmentByTag(UserPanelFragment.CLASS_TAG) as UserPanelFragment?
        if(fragment != null && fragment.isVisible) return

        popFragmentsRecursively()

        supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentLayout, UserPanelFragment.getInstance(), UserPanelFragment.CLASS_TAG)
            .addToBackStack(UserPanelFragment.CLASS_TAG)
            .commit()
        Timber.i("Lunched UserPanelFragment")
    }

    private fun popFragmentsRecursively(){
        val fragments = supportFragmentManager.fragments.count()

        for (i in 0 until fragments){
            supportFragmentManager.popBackStack()
        }
    }

    fun switchToImageActivity(imgUrl: String) {
        val intent = Intent(this, ImageActivity::class.java)
        intent.putExtra(ImageActivity.IMAGE_URL_BUNDLE_KEY, imgUrl)
        startActivity(intent)

        Timber.i("Lunched ImageActivity")
    }

    fun openChooser(){
        val externalStorageCheck = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        val cameraCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if (externalStorageCheck != PackageManager.PERMISSION_GRANTED ||
            cameraCheck != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_STORAGE_PERMISSION_CODE)
        } else {
            Pix.start(this,options)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == CAMERA_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults [1] == PackageManager.PERMISSION_GRANTED){
                Pix.start(this, options)
            }else{
                Toast.makeText(this, getString(R.string.camera_externalStorage_refused),
                    Toast.LENGTH_LONG).show()
            }
            return
        }
    }

    fun clearImages() {
        returnedImages.clear()
    }

    fun removeImageFromPosition(position: Int){
        returnedImages.removeAt(position)
    }

    fun changeThumbnail(position: Int) {
        Collections.swap(returnedImages, position, 0)
    }

    /**
     * Google Maps app navigation intent
     */
    fun openGoogleMaps(lat: Float, lng: Float, label: String)
    {
        if (isPackageInstalled("com.google.android.apps.maps", packageManager))
        {
            val uri = "geo:0,0?q=$lat,$lng(${label.replace(' ', '+')})"
            val navigationIntentUri = Uri.parse(uri)
            val mapIntent = Intent(Intent.ACTION_VIEW, navigationIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")

            try
            {
                startActivity(mapIntent)
            }
            catch (e: ActivityNotFoundException)
            {
                Timber.e("Error launching Google Maps")
            }
        }
        else
        {
            Timber.i("Google maps not installed, forcing maps into user face...")
            googleMapsInstallIntent()
        }
    }

    /**
     * Check package available
     */
    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean
    {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e("%s not installed", packageName)
            false
        }
    }

    /**
     * Opens dialog with GoogleMaps Store
     */
    private fun googleMapsInstallIntent()
    {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.install_maps_dialog_title))
            .setMessage(getString(R.string.install_maps_dialog_msg))
            .setPositiveButton(getString(R.string.install_maps_dialog_yes)) { _, _ -> openMapsStore() }
            .setNegativeButton(getString(R.string.install_maps_dialog_no), null)
            .show()
    }

    /**
     * Lunch PlayStore intent for Google Maps
     */
    private fun openMapsStore()
    {
        val url = "https://play.google.com/store/apps/details?id=com.google.android.apps.maps&hl=pl"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    companion object {
        const val MAX_IMAGES_TO_UPLOAD = 8
        const val GET_IMAGES_REQUEST_CODE = 122
        const val CAMERA_STORAGE_PERMISSION_CODE = 743
    }
}
