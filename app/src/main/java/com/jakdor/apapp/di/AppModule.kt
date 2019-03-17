package com.jakdor.apapp.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.jakdor.apapp.App
import com.jakdor.apapp.arch.ViewModelFactory
import com.jakdor.apapp.common.repository.StackRepository
import com.jakdor.apapp.network.RetrofitFactory
import com.jakdor.apapp.utils.RxSchedulersFacade
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * App-wide dependencies injections
 */
@Module(subcomponents = [ViewModelSubComponent::class])
class AppModule {

    @Provides
    fun provideContext(app: App): Context {
        return app.applicationContext
    }

    @Singleton
    @Provides
    fun provideViewModelFactory(
        viewModelBuilder: ViewModelSubComponent.Builder): ViewModelProvider.Factory {
        return ViewModelFactory(viewModelBuilder.build())
    }

    @Provides
    fun provideRetrofitFactory(): RetrofitFactory {
        return RetrofitFactory()
    }

    @Provides
    fun provideRxSchedulersFacade(): RxSchedulersFacade {
        return RxSchedulersFacade()
    }

    @Provides
    fun provideStackRepository(): StackRepository {
        return StackRepository(
            provideRetrofitFactory(),
            provideRxSchedulersFacade())
    }
}