package com.mrh.recetario

import androidx.compose.ui.window.ComposeUIViewController
import com.mrh.recetario.di.sharedModules
import org.koin.core.context.startKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        startKoin {
            modules(sharedModules)
        }
    }
) {
    App()
}
