package ru.alexey.ndimmatrix.database.schema

import org.jetbrains.exposed.sql.Database

class DatabaseInitializer(

) {
    data class DatabaseConfig(
        val url: String,
        val driver: String,
        val user: String,
        val password: String,
    )

    fun initialize(config: DatabaseConfig) {
        Database.connect(
            url = config.url,
            driver = config.driver,
            user = config.user,
            password = config.password,
        )
    }
}