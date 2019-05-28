package com.jakdor.apapp.network

import com.jakdor.apapp.common.repository.AuthRepository
import io.reactivex.Observable
import timber.log.Timber
import java.net.ConnectException
import javax.inject.Inject

/**
 * Automatic handling of calls requiring auth header
 */
@Suppress("UNREACHABLE_CODE")
class BearerAuthWrapper
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val authRepository: AuthRepository,
                    private val networkManager: NetworkManager){

    var apiAuthService: BackendService = retrofitFactory.createService(
        BackendService.API_URL, BackendService::class.java, authRepository.getBearerToken())

    var authInterceptor: AuthenticationInterceptor = retrofitFactory.authenticationInterceptor

    fun <S> wrapCall(observableCall: Observable<S>): Observable<S> {
        return Observable.create<S> {

            if(!networkManager.checkNetworkStatus()){
                authRepository.noInternet()
                it.onError(throw Exception("No Internet"))
                it.onComplete()
            }

            var callResponse: S? = null

            try {
                callResponse = observableCall.blockingFirst()
            } catch (e: Exception) {
                if (e.message != null && e.message!!.contains("401")) {
                    Timber.i("Invalid bearer token")

                    try{
                        val refreshResponse = authRepository.refreshBearerToken().blockingFirst()

                        if(refreshResponse != null){
                            authInterceptor.authToken = authRepository.getBearerToken()
                            callResponse = observableCall.blockingFirst()
                        }

                    } catch (e2: Exception) {
                        Timber.i("Refresh token failed")
                        authRepository.forceLogout()
                        it.onError(throw Exception("Unauthorized"))
                    }
                }
                else if(e::class.java == ConnectException::class.java){
                    authRepository.requestFailed()
                    it.onError(throw Exception("API request failed"))
                }
            }

            if (callResponse != null) {
                it.onNext(callResponse)
            }

            it.onComplete()
        }
    }
}