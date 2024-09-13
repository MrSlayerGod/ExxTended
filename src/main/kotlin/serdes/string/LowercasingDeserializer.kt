package serdes.string

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import serdes.TransformingSerializer

object LowercasingDeserializer: KSerializer<String> by TransformingSerializer(
    bySerializer = String.serializer(),
    transform = String::lowercase
)