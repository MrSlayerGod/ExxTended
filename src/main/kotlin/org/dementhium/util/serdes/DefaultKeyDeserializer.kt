package nb.util.serdes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.encoding.Decoder

class DefaultKeyDeserializer<K>(
    val detectByKey: K,
    val bySerializer: KSerializer<K>,
    val defaultStatus: DefaultStatus
): KSerializer<K> by bySerializer {

    override fun deserialize(decoder: Decoder): K {

        val deserialized = bySerializer.deserialize(decoder)

        if (defaultStatus.notSet && deserialized == detectByKey) {
            defaultStatus.promote()
        }

        return deserialized
    }
}
