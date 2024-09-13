package serdes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder

class DefaultValueSetter<K>(
    val bySerializer: KSerializer<K>,
    val defaultStatus: DefaultStatus,
    inline val setValue: (K) -> Unit
): KSerializer<K> by bySerializer {

    override fun deserialize(decoder: Decoder): K {

        val deserialized = bySerializer.deserialize(decoder)

        if (defaultStatus.found) {
            setValue(deserialized)
            defaultStatus.promote()
        }

        return deserialized
    }
}
