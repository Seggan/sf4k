package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * A serializer that delegates serialization and deserialization to another serializer.
 *
 * @param T The type to serialize and deserialize.
 * @param S The type to serialize and deserialize to.
 * @param delegate The serializer to delegate to.
 * @param descriptorName The name of the descriptor. Defaults to the simple name of the class.
 */
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

    /**
     * Converts a value of type [T] to a value of type [S].
     * The [S] will then be serialized by the delegate serializer.
     *
     * @param value The value to convert.
     * @return The converted value.
     */
    abstract fun toData(value: T): S

    /**
     * Converts a value of type [S] to a value of type [T].
     * The [S] will be deserialized by the delegate serializer.
     *
     * @param value The value to convert.
     * @return The converted value.
     */
    abstract fun fromData(value: S): T
}