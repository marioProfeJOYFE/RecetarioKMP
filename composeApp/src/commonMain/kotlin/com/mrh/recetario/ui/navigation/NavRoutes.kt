package com.mrh.recetario.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Detail(val mealId: String)

@Serializable
object Vault

@Serializable
object CreateRecipe
