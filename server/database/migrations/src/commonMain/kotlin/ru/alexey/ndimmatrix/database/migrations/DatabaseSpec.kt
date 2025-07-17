package ru.alexey.ndimmatrix.database.migrations

import com.uchuhimo.konf.ConfigSpec

object DatabaseSpec: ConfigSpec("database") {
    val url         by required<String>()
    val driver      by required<String>()
    val user        by required<String>()
    val password    by required<String>()
}