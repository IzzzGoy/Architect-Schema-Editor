package ru.alexey.ndimmatrix.database.migrations

import com.uchuhimo.konf.Config
import com.uchuhimo.konf.source.yaml
import ru.alexey.ndimmatrix.database.schema.DatabaseInitializer

fun main() {
    val config = Config { addSpec(DatabaseSpec) }
        .from.yaml.resource("local_config.yaml")
        .from.env()
    DatabaseInitializer()
        .initialize(
            DatabaseInitializer.DatabaseConfig(
                url = config[DatabaseSpec.url],
                driver = config[DatabaseSpec.driver],
                user = config[DatabaseSpec.user],
                password = config[DatabaseSpec.password],
            )
        )
}