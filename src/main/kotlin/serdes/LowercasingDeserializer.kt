package serdes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer

object LowercasingDeserializer: KSerializer<String> by TransformingSerializer(
    bySerializer = String.serializer(),
    transform = String::lowercase
)