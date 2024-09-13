package serdes.loader

import com.charleskorn.kaml.SingleLineStringStyle
import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import kotlinx.serialization.KSerializer
import kotlinx.serialization.StringFormat
import kotlinx.serialization.json.Json
import java.io.File

enum class ConfigLoaderType(val loaderFormat: StringFormat, val defaultExtension: String) {
    JsonLoader(Json.Default, "json"),
    YamlLoader(Yaml.default, "yaml"),
    YamlLoaderNoQuotes(
        Yaml(
            configuration = YamlConfiguration(
                singleLineStringStyle = SingleLineStringStyle.Plain
            )
        ),
        defaultExtension = "yaml"
    );

    fun <T> load(
        serializer: KSerializer<T>,
        fileName: String,
        filePath: String = ConfigBootstrap.config_path,
    ): T {
        val file = File("$filePath$fileName.$defaultExtension").readText()
        return loaderFormat.decodeFromString(serializer, file)
    }

    fun <T> save(
        serializer: KSerializer<T>,
        objToSave: T,
        fileName: String,
        filePath: String = ConfigBootstrap.config_path
    ) {
        val file = File("$filePath$fileName.$defaultExtension")
        file.writeText(loaderFormat.encodeToString(serializer, objToSave))
    }
}