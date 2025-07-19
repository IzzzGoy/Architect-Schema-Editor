package ru.alexey.ndimmatrix.generator.data.api.local

import android.content.Context
import ru.alexey.ndimmatrix.generator.data.api.LocalStoragePathProvider

class LocalStoragePathProviderImpl(
    private val context: Context
): LocalStoragePathProvider {
    override fun provide(): String {
        return context.cacheDir.absolutePath
    }
}