package ru.alexey.ndimmatrix.generator.presentation.api.models

import arrow.optics.optics
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@optics
data class ArgumentModel(
    val id: Int = ArgCounter.next(),
    val name: String = "NewArgument",
    val type: ArgumentType = ArgumentType.STRING,
    val nullable: Boolean = false,
) {
    fun prettyPrint(): String {
        return buildString {
            append("◈ $name: $type${if (nullable) "?" else ""}")
            append(" (id: $id)")
        }
    }

    companion object
}

private object ArgCounter {
    private var count = 0

    fun next(): Int {
        count + 1
        return count
    }
}