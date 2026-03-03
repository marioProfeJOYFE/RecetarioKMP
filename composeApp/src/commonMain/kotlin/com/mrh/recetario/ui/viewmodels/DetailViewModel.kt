package com.mrh.recetario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrh.recetario.database.relation.RecipeWithIngredients
import com.mrh.recetario.network.dto.MealDto
import com.mrh.recetario.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DetailUiState(
    val meal: MealDto? = null,
    val localRecipe: RecipeWithIngredients? = null,
    val isSaved: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
)

class DetailViewModel(
    private val mealId: String,
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    val isCustomRecipe: Boolean = mealId.startsWith("custom_")

    init {
        loadMeal()
        observeIsSaved()
    }

    private fun loadMeal() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                if (isCustomRecipe) {
                    val local = repository.getLocalRecipe(mealId)
                    _uiState.value = _uiState.value.copy(
                        localRecipe = local,
                        isLoading = false,
                    )
                } else {
                    val meal = repository.getMealById(mealId)
                    _uiState.value = _uiState.value.copy(meal = meal, isLoading = false)
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Sin conexión a internet",
                    isLoading = false,
                )
            }
        }
    }

    private fun observeIsSaved() {
        viewModelScope.launch {
            repository.isRecipeSaved(mealId).collect { saved ->
                _uiState.value = _uiState.value.copy(isSaved = saved)
            }
        }
    }

    fun toggleSave() {
        viewModelScope.launch {
            if (isCustomRecipe) {
                if (_uiState.value.isSaved) {
                    repository.deleteRecipe(mealId)
                }
                // Custom recipes are already saved; toggling off deletes them
            } else {
                val meal = _uiState.value.meal ?: return@launch
                if (_uiState.value.isSaved) {
                    repository.deleteRecipe(meal.idMeal)
                } else {
                    repository.saveRecipe(meal)
                }
            }
        }
    }
}
