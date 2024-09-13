package serdes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder

class TransformingSerializer<T>(
    val bySerializer: KSerializer<T>,
    inline val transform: (T) -> T
): KSerializer<T> by bySerializer {
    override fun deserialize(decoder: Decoder): T = transform(bySerializer.deserialize(decoder))
}
