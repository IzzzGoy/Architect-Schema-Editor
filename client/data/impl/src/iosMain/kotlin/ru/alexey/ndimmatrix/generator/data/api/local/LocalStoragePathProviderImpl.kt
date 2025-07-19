package ru.alexey.ndimmatrix.generator.data.api.local

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask
import ru.alexey.ndimmatrix.generator.data.api.LocalStoragePathProvider

class LocalStoragePathProviderImpl: LocalStoragePathProvider {

    @OptIn(ExperimentalForeignApi::class)
    override fun provide(): String {
        val fileManager: NSFileManager = NSFileManager.defaultManager
        return fileManager.URLForDirectory(
            directory = NSCachesDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        ).toString()
    }

}