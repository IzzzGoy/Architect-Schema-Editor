package ru.alexey.ndimmatrix.generator.data.api

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

interface Storage<T> {
    val data: Flow<T>

    suspend fun update(data: T)
}