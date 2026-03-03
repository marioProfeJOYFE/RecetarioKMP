package com.mrh.recetario.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mrh.recetario.network.dto.toIngredientPairs
import com.mrh.recetario.ui.components.ErrorMessage
import com.mrh.recetario.ui.components.LoadingIndicator
import com.mrh.recetario.ui.viewmodels.DetailViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    mealId: String,
    onBack: () -> Unit,
    viewModel: DetailViewModel = koinViewModel(parameters = { parametersOf(mealId) }),
) {
    val state by viewModel.uiState.collectAsState()

    // Extract display data from either source
    val title = state.meal?.strMeal ?: state.localRecipe?.recipe?.title
    val category = state.meal?.strCategory ?: state.localRecipe?.recipe?.category
    val area = state.meal?.strArea ?: state.localRecipe?.recipe?.area
    val instructions = state.meal?.strInstructions ?: state.localRecipe?.recipe?.instructions
    val imageUrl = state.meal?.strMealThumb ?: state.localRecipe?.recipe?.imageUrl
    val ingredientPairs = state.meal?.toIngredientPairs()
        ?: state.localRecipe?.ingredients?.map { it.name to it.measure }

    val hasContent = state.meal != null || state.localRecipe != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title ?: "Detalle") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            if (hasContent) {
                if (viewModel.isCustomRecipe) {
                    FloatingActionButton(
                        onClick = {
                            viewModel.toggleSave()
                            onBack()
                        },
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar receta",
                        )
                    }
                } else {
                    FloatingActionButton(
                        onClick = { viewModel.toggleSave() },
                        containerColor = if (state.isSaved)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant,
                    ) {
                        Icon(
                            imageVector = if (state.isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = if (state.isSaved) "Quitar de favoritos" else "Guardar en favoritos",
                        )
                    }
                }
            }
        }
    ) { padding ->
        when {
            state.isLoading -> LoadingIndicator(modifier = Modifier.padding(padding))
            state.error != null -> ErrorMessage(
                message = state.error!!,
                onRetry = {},
                modifier = Modifier.padding(padding),
            )
            hasContent -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (!imageUrl.isNullOrBlank()) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 9f),
                        )
                    }

                    Column(modifier = Modifier.padding(16.dp)) {
                        if (!category.isNullOrBlank() || !area.isNullOrBlank()) {
                            Text(
                                text = listOfNotNull(
                                    category?.takeIf { it.isNotBlank() },
                                    area?.takeIf { it.isNotBlank() },
                                ).joinToString(" - "),
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.primary,
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        if (!ingredientPairs.isNullOrEmpty()) {
                            Text(
                                text = "Ingredientes",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            ingredientPairs.forEach { (ingredient, measure) ->
                                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                                    Text(
                                        text = ingredient,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Medium,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = measure,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        if (!instructions.isNullOrBlank()) {
                            Text(
                                text = "Instrucciones",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = instructions,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }

                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
