package com.mrh.recetario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrh.recetario.database.relation.RecipeWithIngredients
import com.mrh.recetario.repository.RecipeRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class VaultViewModel(repository: RecipeRepository) : ViewModel() {

    val savedRecipes: StateFlow<List<RecipeWithIngredients>> =
        repository.getSavedRecipes().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}
