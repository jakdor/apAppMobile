package com.jakdor.apapp.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import timber.log.Timber
import javax.inject.Inject

class NetworkManager
@Inject constructor(private val context: Context){

    /**
     * Check network status
     * @return boolean - network status
     */
    fun checkNetworkStatus(): Boolean {
        val connectivityManager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkInfo = connectivityManager.activeNetworkInfo
        return if (networkInfo == null) {
            Timber.e("Internet status: no service!")
            false
        } else {
            Timber.i("Internet status: OK")
            true
        }
    }
}