package com.mrh.recetario.repository

import com.mrh.recetario.database.dao.RecipeDao
import com.mrh.recetario.database.entity.IngredientEntity
import com.mrh.recetario.database.entity.RecipeEntity
import com.mrh.recetario.database.relation.RecipeWithIngredients
import com.mrh.recetario.network.RecipeApiService
import com.mrh.recetario.network.dto.MealDto
import com.mrh.recetario.network.dto.toIngredientPairs
import kotlinx.coroutines.flow.Flow

class RecipeRepository(
    private val apiService: RecipeApiService,
    private val recipeDao: RecipeDao,
) {
    suspend fun getRandomMeal(): MealDto? = apiService.getRandomMeal()

    suspend fun searchMeals(query: String): List<MealDto> = apiService.searchMeals(query)

    suspend fun getMealById(id: String): MealDto? = apiService.getMealById(id)

    fun getSavedRecipes(): Flow<List<RecipeWithIngredients>> =
        recipeDao.getAllRecipesWithIngredients()

    fun isRecipeSaved(id: String): Flow<Boolean> = recipeDao.isRecipeSaved(id)

    suspend fun saveRecipe(meal: MealDto) {
        val recipe = RecipeEntity(
            id = meal.idMeal,
            title = meal.strMeal,
            instructions = meal.strInstructions ?: "",
            imageUrl = meal.strMealThumb ?: "",
            category = meal.strCategory ?: "",
            area = meal.strArea ?: "",
        )
        val ingredients = meal.toIngredientPairs().map { (name, measure) ->
            IngredientEntity(
                recipeId = meal.idMeal,
                name = name,
                measure = measure,
            )
        }
        recipeDao.insertRecipeWithIngredients(recipe, ingredients)
    }

    suspend fun saveCustomRecipe(
        title: String,
        category: String,
        area: String,
        instructions: String,
        ingredients: List<Pair<String, String>>,
    ) {
        val id = "custom_${kotlin.random.Random.nextLong(1_000_000_000L, 9_999_999_999L)}"
        val recipe = RecipeEntity(
            id = id,
            title = title,
            instructions = instructions,
            imageUrl = "",
            category = category,
            area = area,
        )
        val ingredientEntities = ingredients.map { (name, measure) ->
            IngredientEntity(
                recipeId = id,
                name = name,
                measure = measure,
            )
        }
        recipeDao.insertRecipeWithIngredients(recipe, ingredientEntities)
    }

    suspend fun getLocalRecipe(id: String): RecipeWithIngredients? =
        recipeDao.getRecipeWithIngredients(id)

    suspend fun deleteRecipe(id: String) = recipeDao.deleteRecipe(id)
}
