package serdes.loader

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import kotlin.reflect.KClass
import kotlin.reflect.full.createType


abstract class ConfigLoader<T> (
    val loaderType: ConfigLoaderType,
    val fileName: String,
    val filePath: String = ConfigBootstrap.config_path,
    inline val getSerializer: () -> KSerializer<T>,
    inline val init: T.() -> Unit = { }
) {

    constructor(
        loaderType: ConfigLoaderType,
        fileName: String,
        filePath: String = ConfigBootstrap.config_path,
        configClass: KClass<*>,
        init: T.() -> Unit = { }
    ): this(
        loaderType = loaderType,
        fileName = fileName,
        filePath = filePath,
        getSerializer = { serializer(configClass.createType()) as KSerializer<T> },
        init = init,
    )


    val INSTANCE by lazy {

        val instance = loaderType.load(
            serializer = getSerializer(),
            fileName = fileName,
            filePath = filePath
        )

        instance.apply(init)
    }

    inline operator fun invoke() = INSTANCE

    fun load(): T = loaderType.load(getSerializer(), fileName, filePath)

    fun save(obj: T) = loaderType.save(getSerializer(), obj, fileName, filePath)
}