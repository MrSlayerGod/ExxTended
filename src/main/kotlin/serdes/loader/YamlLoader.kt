package serdes.loader

import kotlinx.serialization.*
import kotlin.reflect.KClass
import kotlin.reflect.full.createType

abstract class YamlLoader<T>(
    fileName: String,
    quotes: Boolean = true,
    filePath: String = ConfigBootstrap.config_path,
    getSerializer: () -> KSerializer<T>,
    init: T.() -> Unit = { }
): ConfigLoader<T>(
    loaderType = if (quotes) ConfigLoaderType.YamlLoader else ConfigLoaderType.YamlLoaderNoQuotes,
    fileName = fileName,
    filePath = filePath,
    getSerializer = getSerializer,
    init = init
) {
    constructor(
        fileName: String,
        filePath: String = ConfigBootstrap.config_path,
        configClass: KClass<*>,
        init: T.() -> Unit = { }
    ): this(
        fileName = fileName,
        filePath = filePath,
        getSerializer = { serializer(configClass.createType()) as KSerializer<T> },
        init = init,
    )
}
