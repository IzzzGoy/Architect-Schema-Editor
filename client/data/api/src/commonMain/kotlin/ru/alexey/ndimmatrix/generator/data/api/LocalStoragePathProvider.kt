package ru.alexey.ndimmatrix.generator.data.api

fun interface LocalStoragePathProvider {
    fun provide(): String
}