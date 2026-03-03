package com.mrh.recetario.network

import com.mrh.recetario.network.dto.MealDto
import com.mrh.recetario.network.dto.MealResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class RecipeApiService(private val httpClient: HttpClient) {

    companion object {
        private const val BASE_URL = "https://www.themealdb.com/api/json/v1/1"
    }

    suspend fun getRandomMeal(): MealDto? {
        val response: MealResponse = httpClient.get("$BASE_URL/random.php").body()
        return response.meals?.firstOrNull()
    }

    suspend fun searchMeals(query: String): List<MealDto> {
        val response: MealResponse = httpClient.get("$BASE_URL/search.php") {
            parameter("s", query)
        }.body()
        return response.meals ?: emptyList()
    }

    suspend fun getMealById(id: String): MealDto? {
        val response: MealResponse = httpClient.get("$BASE_URL/lookup.php") {
            parameter("i", id)
        }.body()
        return response.meals?.firstOrNull()
    }
}
