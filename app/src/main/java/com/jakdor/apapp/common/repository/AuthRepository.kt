package com.jakdor.apapp.common.repository

import android.util.Base64
import com.google.gson.Gson
import com.jakdor.apapp.common.model.auth.*
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.RetrofitFactory
import io.reactivex.Observable
import java.nio.charset.Charset
import javax.inject.Inject

class AuthRepository
@Inject constructor(private val retrofitFactory: RetrofitFactory,
                    private val preferencesRepository: PreferencesRepository){

    private var loginStr: String = ""
    private var bearerToken : String = ""
    private var refreshToken : String = ""

    init {
        getTokens()
    }

    private val apiService: BackendService =
        retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)
    
    fun getBearerToken(): String{
        return bearerToken
    }
    
    fun login(login: String, password: String): Observable<Boolean> {
        return apiService.postLogin(LoginRequest(login, password))
            .doOnNext {
                if(it != null && it.error == null){
                    this.loginStr = login
                    bearerToken = it.accessToken ?: ""
                    refreshToken = it.refreshToken ?: ""
                    saveTokens()
                }
            }
            .flatMap { 
                t: LoginResponse? -> Observable.just(t != null && t.error == null)
            }
    }

    fun refreshBearerToken(): Observable<String?> {
        return apiService.postRefresh(RefreshRequest(loginStr, bearerToken))
            .doOnNext {
                if(it != null){
                    bearerToken = it.accessToken
                    saveTokens()
                }
            }
            .flatMap {
                t: RefreshResponse? ->  Observable.just(t?.accessToken)
            }
    }

    fun saveTokens(){
        val tokenObj = TokenStorageModel(bearerToken, refreshToken)
        val tokenSerialized = Gson().toJson(tokenObj)
        val tokenHashed = Base64.encode(tokenSerialized.toByteArray(
            Charset.defaultCharset()), Base64.DEFAULT).toString(Charset.defaultCharset())
        preferencesRepository.saveAsync(TOKEN_KEY, tokenHashed)
        preferencesRepository.saveAsync(LOGIN_KEY, loginStr)
    }

    fun getTokens(){
        val tokenHashed = preferencesRepository.getString(TOKEN_KEY)
        if(tokenHashed.isEmpty()) return
        val tokenSerialized = Base64.decode(tokenHashed.toByteArray(
            Charset.defaultCharset()), Base64.DEFAULT).toString(Charset.defaultCharset())
        val tokenObj = Gson().fromJson(tokenSerialized, TokenStorageModel::class.java)
        bearerToken = tokenObj.accessToken
        refreshToken = tokenObj.refreshToken
        loginStr = preferencesRepository.getString(LOGIN_KEY)
    }

    companion object {
        private const val TOKEN_KEY = "tokenStorageKey"
        private const val LOGIN_KEY = "loginStorageKey"
    }
}