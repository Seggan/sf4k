package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@OptIn(ExperimentalSerializationApi::class)
abstract class DelegatingSerializer<T, S>(
    private val delegate: KSerializer<S>,
    descriptorName: String? = null
) : KSerializer<T> {

    final override val descriptor = SerialDescriptor(
        descriptorName ?: javaClass.simpleName,
        delegate.descriptor
    )

    final override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeSerializableValue(delegate, toData(value))
    }

    final override fun deserialize(decoder: Decoder): T {
        return fromData(decoder.decodeSerializableValue(delegate))
    }

    abstract fun toData(value: T): S

    abstract fun fromData(value: S): T
}