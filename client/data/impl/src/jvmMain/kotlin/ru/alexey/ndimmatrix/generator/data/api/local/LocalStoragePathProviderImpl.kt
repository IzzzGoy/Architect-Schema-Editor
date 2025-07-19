package ru.alexey.ndimmatrix.generator.data.api.local

import net.harawata.appdirs.AppDirsFactory
import ru.alexey.ndimmatrix.generator.data.api.LocalStoragePathProvider

class LocalStoragePathProviderImpl: LocalStoragePathProvider {

    override fun provide(): String {
        return AppDirsFactory.getInstance().getUserCacheDir(PACKAGE_NAME, VERSION, ORGANISATION)
    }

    companion object {
        private const val PACKAGE_NAME = "ru.alexey.ndimmatrix.generator"
        private const val VERSION = "1.0"
        private const val ORGANISATION = "alexey"
    }
}