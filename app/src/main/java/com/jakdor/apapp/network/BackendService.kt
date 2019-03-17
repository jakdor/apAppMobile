package com.jakdor.apapp.network

import com.jakdor.apapp.common.model.StackQuestions
import io.reactivex.Observable
import retrofit2.http.GET

interface BackendService {

    @GET("2.2/questions?order=desc&sort=activity&tagged=Android&site=stackoverflow")
    fun getQuestions(): Observable<StackQuestions>

    companion object {
        const val API_URL = "https://api.stackexchange.com/"
    }
}