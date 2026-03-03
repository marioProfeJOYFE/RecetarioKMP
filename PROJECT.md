# Contexto de Proyecto: Recetario (Kotlin Multiplatform Demo)

## 1. Visión General
**Recetario** es una aplicación móvil de recetario digital diseñada para demostrar el estado del arte de Kotlin Multiplatform (KMP). La app consume una API REST pública de recetas, permite a los usuarios explorar platos y guardar sus favoritos en una base de datos local para acceso sin conexión (offline-first).

## 2. Objetivo Principal del Demo
Demostrar que en el ecosistema actual de KMP es posible escribir prácticamente el 100% de la aplicación en el source set `commonMain`. Esto incluye no solo la lógica de negocio, sino también la Interfaz de Usuario, las peticiones de red, la persistencia de datos relacionales y la carga de imágenes.

## 3. Stack Tecnológico (Core en `commonMain`)
* **Interfaz de Usuario:** Compose Multiplatform.
* **Red (API REST):** Ktor Client + `kotlinx.serialization` (JSON).
* **Persistencia Local:** Room para KMP (versión 2.7.0+).
* **Carga de Imágenes:** Coil3 (soporte oficial multiplataforma).
* **Inyección de Dependencias:** Koin.
* **Reactividad y Estado:** Coroutines, `StateFlow` y `lifecycle-viewmodel` (oficial de KMP).

## 4. Arquitectura de Software
El proyecto sigue los principios de **Clean Architecture** simplificada y el patrón **MVVM** (Model-View-ViewModel):
* **Capa de Presentación:** ViewModels compartidos que exponen un estado reactivo (`StateFlow`) a las pantallas en Compose.
* **Capa de Datos:** Un patrón `Repository` que actúa como Única Fuente de Verdad (Single Source of Truth), orquestando las llamadas entre Ktor (remoto) y Room (local).

## 5. Esquema de Base de Datos Relacional (Room KMP)
Se implementa una relación **Uno a Muchos (1:N)** utilizando Room en `commonMain`:
* **Tabla `recipes`:** Almacena los metadatos de la receta (id, título, instrucciones, URL de imagen).
* **Tabla `ingredients`:** Almacena los ingredientes vinculados a una receta (`recipeId` como Foreign Key con borrado en cascada `CASCADE`).
* **POJO de Relación:** Clase `@Embedded` + `@Relation` para recuperar una receta con todos sus ingredientes en una sola consulta.
* **DAO:** Retorna un `Flow<List<RecipeWithIngredients>>` para que la UI reaccione automáticamente a las inserciones.

## 6. Flujo de Datos (Offline-First)
1. **Exploración:** El ViewModel solicita al Repositorio recetas aleatorias. El Repositorio hace un *fetch* mediante Ktor y las devuelve a la UI.
2. **Guardado (Bóveda):** Al marcar una receta como favorita, el Repositorio inserta la receta y sus ingredientes en Room a través del DAO (usando una `@Transaction`).
3. **Lectura Offline:** La pantalla "Mi Recetario" observa directamente el `Flow` de Room. Si no hay internet, la app sigue siendo 100% funcional leyendo de SQLite.

## 7. Estructura de Carpetas Propuesta (`commonMain`)
* `di/`: Configuración de Koin (Módulos de app, red y base de datos).
* `network/`: Ktor client, endpoints y Data Transfer Objects (DTOs).
* `database/`: Entidades de Room, DAOs y la clase `AppDatabase`.
* `repository/`: Implementación de la lógica de sincronización.
* `ui/`:
    * `screens/`: Composables de pantalla (Home, Detail, Vault).
    * `viewmodels/`: Gestión de estado de la UI.
    * `components/`: Composables reutilizables (tarjetas, botones).

## 8. Código Específico de Plataforma (Lo mínimo indispensable)
El agente debe minimizar el código fuera de `commonMain`. Lo único permitido en los source sets nativos es:
* **`androidMain`**: `MainActivity.kt` (punto de entrada Compose) y el Database Builder de Room (requiere inyectar el `Context`).
* **`iosMain`**: `MainViewController.kt` (punto de entrada Skico/UIKit) y el Database Builder de Room (requiere instanciar la ruta de SQLite usando `NSFileManager`).