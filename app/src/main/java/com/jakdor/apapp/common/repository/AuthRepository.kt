package com.jakdor.apapp.common.repository

import android.util.Base64
import com.google.gson.Gson
import com.jakdor.apapp.common.model.auth.*
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.RetrofitFactory
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import timber.log.Timber
import java.nio.charset.Charset
import javax.inject.Inject

class AuthRepository
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val preferencesRepository: PreferencesRepository){

    private var isLogged: Boolean = false
    private var loginStr: String = ""
    private var bearerToken : String = ""
    private var refreshToken : String = ""

    val authStatusSubject = PublishSubject.create<AuthStatusEnum>()

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
                    authStatusSubject.onNext(AuthStatusEnum.LOGGED_IN)
                }
            }
            .flatMap { 
                t: LoginResponse? -> Observable.just(t != null && t.error == null)
            }
    }

    fun register(login: String, email: String, password: String, name: String, surname: String):
            Observable<RegisterStatusEnum>{
        return apiService.postRegister(RegisterRequest(login, email, password, name, surname))
            .flatMap {
                    t: RegisterResponse? -> Observable.just(t?.registerStatus)
            }
    }

    fun refreshBearerToken(): Observable<String?> {
        return apiService.postRefresh(RefreshRequest(loginStr, refreshToken))
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

    private fun saveTokens(){
        val tokenObj = TokenStorageModel(bearerToken, refreshToken)
        val tokenSerialized = Gson().toJson(tokenObj)
        val tokenHashed = Base64.encode(tokenSerialized.toByteArray(
            Charset.defaultCharset()), Base64.DEFAULT).toString(Charset.defaultCharset())
        preferencesRepository.saveAsync(TOKEN_KEY, tokenHashed)
        preferencesRepository.saveAsync(LOGIN_KEY, loginStr)
    }

    private fun getTokens(){
        val tokenHashed = preferencesRepository.getString(TOKEN_KEY)
        if(tokenHashed.isEmpty()){
            isLogged = false
            return
        }
        val tokenSerialized = Base64.decode(tokenHashed.toByteArray(
            Charset.defaultCharset()), Base64.DEFAULT).toString(Charset.defaultCharset())
        val tokenObj = Gson().fromJson(tokenSerialized, TokenStorageModel::class.java)
        bearerToken = tokenObj.accessToken
        refreshToken = tokenObj.refreshToken
        loginStr = preferencesRepository.getString(LOGIN_KEY)
        isLogged = true
    }

    fun isLoggedIn(): Boolean{
        return isLogged
    }

    fun noInternet(){
        Timber.i("No Internet connection")
        authStatusSubject.onNext(AuthStatusEnum.NO_INTERNET)
    }

    fun requestFailed(){
        Timber.i("Last api request failed")
        authStatusSubject.onNext(AuthStatusEnum.REQUEST_FAILED)
    }

    fun forceLogout(){
        Timber.i("Force logout")
        authStatusSubject.onNext(AuthStatusEnum.LOGOUT_FORCED)
    }

    fun userLogout(){
        Timber.i("User init logout")
        authStatusSubject.onNext(AuthStatusEnum.LOGOUT_USER)
    }

    enum class AuthStatusEnum {
        IDLE, LOGGED_IN, LOGOUT_FORCED, LOGOUT_USER, REQUEST_FAILED, NO_INTERNET
    }

    companion object {
        private const val TOKEN_KEY = "tokenStorageKey"
        private const val LOGIN_KEY = "loginStorageKey"
    }
}