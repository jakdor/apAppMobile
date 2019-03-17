package com.jakdor.apapp.ui.apartmentList

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.jakdor.apapp.arch.BaseViewModel
import com.jakdor.apapp.common.model.StackQuestions
import com.jakdor.apapp.common.repository.StackRepository
import com.jakdor.apapp.utils.RxSchedulersFacade
import timber.log.Timber
import javax.inject.Inject

class ApartmentListViewModel
@Inject constructor(application: Application,
                    rxSchedulersFacade: RxSchedulersFacade,
                    private val stackRepository: StackRepository):
    BaseViewModel(application, rxSchedulersFacade){

    val stackQuestionsLiveData = MutableLiveData<StackQuestions>()

    fun observeStackQuestionsSubject(){
        disposable.add(stackRepository.stackQuestionsSubject
            .observeOn(rxSchedulersFacade.io())
            .subscribeOn(rxSchedulersFacade.io())
            .subscribe({ t: StackQuestions -> stackQuestionsLiveData.postValue(t) },
                {e ->  Timber.e(e, "Error observing StackQuestionsSubject")}))
    }

    fun requestStackQuestionsUpdate(){
        stackRepository.requestStackQuestions()
    }
}