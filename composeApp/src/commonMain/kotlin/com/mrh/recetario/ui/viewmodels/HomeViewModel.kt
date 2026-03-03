package com.mrh.recetario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrh.recetario.network.dto.MealDto
import com.mrh.recetario.repository.RecipeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val meals: List<MealDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
)

class HomeViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadRandomMeals()
    }

    fun loadRandomMeals() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val meals = coroutineScope {
                    (1..10).map {
                        async { runCatching { repository.getRandomMeal() }.getOrNull() }
                    }.awaitAll().filterNotNull().distinctBy { it.idMeal }
                }
                if (meals.isEmpty()) {
                    _uiState.value = _uiState.value.copy(
                        error = "Sin conexión a internet",
                        isLoading = false,
                    )
                } else {
                    _uiState.value = _uiState.value.copy(meals = meals, isLoading = false)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    error = "Sin conexión a internet",
                    isLoading = false,
                )
            }
        }
    }

    fun searchMeals(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            loadRandomMeals()
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val meals = repository.searchMeals(query)
                _uiState.value = _uiState.value.copy(meals = meals, isLoading = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Sin conexión a internet",
                    isLoading = false,
                )
            }
        }
    }
}
