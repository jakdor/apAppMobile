package com.jakdor.apapp.common.repository

import android.app.Application
import android.content.Context
import com.jakdor.apapp.R
import javax.inject.Inject

class PreferencesRepository
@Inject constructor(application: Application){

    private val pref = application.getSharedPreferences(application.getString(R.string.sharedPrefFile), Context.MODE_PRIVATE)

    fun saveAsync(key: String, str: String){
        with(pref.edit()){
            putString(key, str)
            apply()
        }
    }

    fun getString(key: String): String{
        return pref.getString(key, "") ?: ""
    }
}