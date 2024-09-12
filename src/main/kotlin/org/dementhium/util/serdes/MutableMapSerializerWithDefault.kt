package nb.util.serdes

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

class MutableMapSerializerWithDefault<K, V> (
    val defaultKey: K,
    val keySerializer: KSerializer<K>,
    val valueSerializer: KSerializer<V>,
    val defaultStatus: DefaultStatus = DefaultStatus(),
    inline val valueSetter: (V) -> Unit,
    val mapSerializer: KSerializer<Map<K, V>> = MapSerializer(
        keySerializer = DefaultKeyDeserializer(defaultKey, keySerializer, defaultStatus),
        valueSerializer = DefaultValueSetter(valueSerializer, defaultStatus, valueSetter)
    )
): KSerializer<MutableMap<K, V>> {

    override val descriptor: SerialDescriptor get() =
        mapSerializer.descriptor

    override fun deserialize(decoder: Decoder): MutableMap<K, V> =
        mapSerializer.deserialize(decoder).toMutableMap()

    override fun serialize(encoder: Encoder, value: MutableMap<K, V>) =
        mapSerializer.serialize(encoder, value)
}

