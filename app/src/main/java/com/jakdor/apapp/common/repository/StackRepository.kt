package com.jakdor.apapp.common.repository

import com.jakdor.apapp.common.model.StackQuestions
import com.jakdor.apapp.network.BackendService
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import timber.log.Timber
import javax.inject.Inject

class StackRepository
@Inject constructor(retrofitFactory: RetrofitFactory,
                    private val rxSchedulersFacade: RxSchedulersFacade){

    private val apiService: BackendService =
        retrofitFactory.createService(BackendService.API_URL, BackendService::class.java)

    private val rxDisposables: CompositeDisposable = CompositeDisposable()

    val stackQuestionsSubject: BehaviorSubject<StackQuestions> = BehaviorSubject.create()

    fun requestStackQuestions(){
        rxDisposables.add(apiService.getQuestions()
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: StackQuestions? -> if(t != null) stackQuestionsSubject.onNext(t) },
                {e ->  Timber.e(e, "Error observing StackQuestions")}))
    }

    fun dispose(){
        if(!rxDisposables.isDisposed) rxDisposables.dispose()
    }
}