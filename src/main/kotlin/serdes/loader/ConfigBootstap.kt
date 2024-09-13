package serdes.loader

import kotlinx.serialization.builtins.serializer


object ConfigBootstrap {
    val config_path: String by lazy {
        ConfigLoaderType.YamlLoader.load(
            serializer = String.serializer(),
            fileName = "config-path",
            filePath = "./",
        )
    }
}
