package com.mrh.recetario

import android.app.Application
import com.mrh.recetario.di.sharedModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class RecetarioApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RecetarioApp)
            modules(sharedModules)
        }
    }
}
