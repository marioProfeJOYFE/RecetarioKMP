package com.mrh.recetario.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val instructions: String,
    val imageUrl: String,
    val category: String = "",
    val area: String = "",
)
