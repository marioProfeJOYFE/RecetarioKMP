package com.mrh.recetario.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.mrh.recetario.database.AppDatabase
import com.mrh.recetario.network.RecipeApiService
import com.mrh.recetario.network.createHttpClient
import com.mrh.recetario.repository.RecipeRepository
import com.mrh.recetario.ui.viewmodels.CreateRecipeViewModel
import com.mrh.recetario.ui.viewmodels.DetailViewModel
import com.mrh.recetario.ui.viewmodels.HomeViewModel
import com.mrh.recetario.ui.viewmodels.VaultViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val networkModule = module {
    single { createHttpClient() }
    single { RecipeApiService(get()) }
}

val databaseModule = module {
    single<AppDatabase> {
        val builder = get<androidx.room.RoomDatabase.Builder<AppDatabase>>()
        builder
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single { get<AppDatabase>().recipeDao() }
}

val repositoryModule = module {
    single { RecipeRepository(get(), get()) }
}

val viewModelModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { params -> DetailViewModel(params.get(), get()) }
    viewModel { VaultViewModel(get()) }
    viewModel { CreateRecipeViewModel(get()) }
}

val sharedModules = listOf(
    networkModule,
    databaseModule,
    repositoryModule,
    viewModelModule,
    platformModule,
)
