package com.jakdor.apapp.utils

import android.content.Context
import android.util.Log
import com.jakdor.apapp.BuildConfig
import timber.log.Timber

/**
 * Timber logger & Crashlytics libs configuration
 */
object AppLogger {

    fun init(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree(context))
        }
    }

    /**
     * Log tree for production crash
     * todo not implemented
     */
    private class CrashReportingTree(context: Context) : Timber.Tree() {
        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return
            }

            //FakeCrashLibrary.log(priority, tag, message)

            if (t != null) {
                if (priority == Log.ERROR) {
                    //FakeCrashLibrary.logError(t)
                } else if (priority == Log.WARN) {
                    //FakeCrashLibrary.logWarning(t)
                }
            }
        }
    }
}