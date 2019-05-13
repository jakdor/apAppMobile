package com.jakdor.apapp.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.jakdor.apapp.App
import com.jakdor.apapp.arch.ViewModelFactory
import com.jakdor.apapp.common.repository.ApartmentRepository
import com.jakdor.apapp.common.repository.AuthRepository
import com.jakdor.apapp.common.repository.PreferencesRepository
import com.jakdor.apapp.network.BearerAuthWrapper
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
    fun provideContext(app: App): Context = app.applicationContext

    @Singleton
    @Provides
    fun provideViewModelFactory(viewModelBuilder: ViewModelSubComponent.Builder):
            ViewModelProvider.Factory = ViewModelFactory(viewModelBuilder.build())

    @Singleton
    @Provides
    fun provideRetrofitFactory(): RetrofitFactory = RetrofitFactory()

    @Singleton
    @Provides
    fun provideAuthRepository(retrofitFactory: RetrofitFactory,
                              preferencesRepository: PreferencesRepository):
            AuthRepository = AuthRepository(retrofitFactory, preferencesRepository)

    @Provides
    fun providePreferencesRepository(app: Application): PreferencesRepository
            = PreferencesRepository(app)


    @Provides
    fun provideRxSchedulersFacade(): RxSchedulersFacade = RxSchedulersFacade()

    @Singleton
    @Provides
    fun provideApartmentRepository(bearerAuthWrapper: BearerAuthWrapper,
                                   rxSchedulersFacade: RxSchedulersFacade):
            ApartmentRepository = ApartmentRepository(bearerAuthWrapper, rxSchedulersFacade)

    @Provides
    fun provideBearerAuthWrapper(retrofitFactory: RetrofitFactory,
                                 authRepository: AuthRepository):
            BearerAuthWrapper = BearerAuthWrapper(retrofitFactory, authRepository)
}