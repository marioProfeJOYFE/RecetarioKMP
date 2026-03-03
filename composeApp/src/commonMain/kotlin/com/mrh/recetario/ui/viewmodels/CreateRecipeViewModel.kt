package com.mrh.recetario.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrh.recetario.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class IngredientField(
    val name: String = "",
    val measure: String = "",
)

data class CreateRecipeState(
    val title: String = "",
    val category: String = "",
    val area: String = "",
    val instructions: String = "",
    val ingredients: List<IngredientField> = listOf(IngredientField()),
    val isSaving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null,
)

class CreateRecipeViewModel(
    private val repository: RecipeRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CreateRecipeState())
    val state: StateFlow<CreateRecipeState> = _state.asStateFlow()

    fun updateTitle(value: String) {
        _state.update { it.copy(title = value) }
    }

    fun updateCategory(value: String) {
        _state.update { it.copy(category = value) }
    }

    fun updateArea(value: String) {
        _state.update { it.copy(area = value) }
    }

    fun updateInstructions(value: String) {
        _state.update { it.copy(instructions = value) }
    }

    fun addIngredient() {
        _state.update { it.copy(ingredients = it.ingredients + IngredientField()) }
    }

    fun removeIngredient(index: Int) {
        _state.update {
            it.copy(ingredients = it.ingredients.toMutableList().apply { removeAt(index) })
        }
    }

    fun updateIngredient(index: Int, name: String, measure: String) {
        _state.update {
            val updated = it.ingredients.toMutableList()
            updated[index] = IngredientField(name, measure)
            it.copy(ingredients = updated)
        }
    }

    fun saveRecipe() {
        val current = _state.value
        if (current.title.isBlank()) {
            _state.update { it.copy(error = "El título no puede estar vacío") }
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true, error = null) }
            try {
                val ingredientPairs = current.ingredients
                    .filter { it.name.isNotBlank() }
                    .map { it.name to it.measure }

                repository.saveCustomRecipe(
                    title = current.title.trim(),
                    category = current.category.trim(),
                    area = current.area.trim(),
                    instructions = current.instructions.trim(),
                    ingredients = ingredientPairs,
                )
                _state.update { it.copy(isSaving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(isSaving = false, error = "Error al guardar: ${e.message}") }
            }
        }
    }

    fun resetForm() {
        _state.value = CreateRecipeState()
    }
}
