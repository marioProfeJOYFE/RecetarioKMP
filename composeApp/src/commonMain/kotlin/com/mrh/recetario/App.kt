package com.mrh.recetario

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.mrh.recetario.ui.navigation.AppNavigation
import com.mrh.recetario.ui.theme.appColorScheme

@Composable
fun App() {
    val colorScheme = appColorScheme(darkTheme = isSystemInDarkTheme())
    MaterialTheme(colorScheme = colorScheme) {
        AppNavigation()
    }
}
