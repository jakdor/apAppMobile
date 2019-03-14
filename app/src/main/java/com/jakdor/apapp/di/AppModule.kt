package com.jakdor.apapp.di

import android.content.Context
import com.jakdor.apapp.App
import dagger.Module
import dagger.Provides

/**
 * App-wide dependencies injections
 */
@Module(subcomponents = [ViewModelSubComponent::class])
class AppModule {

    @Provides
    fun provideContext(app: App): Context {
        return app.applicationContext
    }
}