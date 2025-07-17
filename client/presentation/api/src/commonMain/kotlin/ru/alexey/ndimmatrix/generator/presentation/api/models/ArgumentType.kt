package ru.alexey.ndimmatrix.generator.presentation.api.models

enum class ArgumentType {
    INT,
    LONG,
    FLOAT,
    DOUBLE,
    BOOLEAN,
    STRING,
    CHAR,
    BYTE,
    SHORT;

    override fun toString(): String = name.lowercase().replaceFirstChar { it.uppercase() }
}
