package com.mrh.recetario.di

import androidx.room.Room
import com.mrh.recetario.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.dsl.module
import platform.Foundation.NSHomeDirectory

actual val platformModule: Module = module {
    single {
        val dbFilePath = NSHomeDirectory() + "/recetario.db"
        Room.databaseBuilder<AppDatabase>(name = dbFilePath)
    }
}
