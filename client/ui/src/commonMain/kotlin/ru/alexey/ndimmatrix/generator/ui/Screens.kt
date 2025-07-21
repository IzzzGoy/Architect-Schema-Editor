package ru.alexey.ndimmatrix.generator.ui

import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data class Params(val project: String)