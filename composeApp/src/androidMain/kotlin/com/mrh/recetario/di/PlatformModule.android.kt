package com.mrh.recetario.di

import androidx.room.Room
import com.mrh.recetario.database.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single {
        Room.databaseBuilder<AppDatabase>(
            context = androidContext(),
            name = androidContext().getDatabasePath("recetario.db").absolutePath
        )
    }
}
