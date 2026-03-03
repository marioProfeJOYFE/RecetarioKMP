package com.mrh.recetario.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.mrh.recetario.database.entity.IngredientEntity
import com.mrh.recetario.database.entity.RecipeEntity

data class RecipeWithIngredients(
    @Embedded val recipe: RecipeEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredients: List<IngredientEntity>
)
