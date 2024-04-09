package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.util.WeakHashMap

/**
 * An [Encoder] that writes data to a [PersistentDataContainer].
 */
@OptIn(ExperimentalSerializationApi::class)
class PersistentDataEncoder internal constructor(
    private val plugin: Plugin,
    private val context: PersistentDataAdapterContext,
    internal val key: NamespacedKey,
    internal val container: PersistentDataContainer
) : Encoder {

    override val serializersModule = EmptySerializersModule()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return PersistentDataCompositeEncoder(plugin, context, this)
    }

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        val encoder = PersistentDataCompositeEncoder(plugin, context, this)
        encoder.encodeSize(collectionSize)
        return encoder
    }

    override fun encodeBoolean(value: Boolean) {
        container.set(key, PersistentDataType.BOOLEAN, value)
    }

    override fun encodeByte(value: Byte) {
        container.set(key, PersistentDataType.BYTE, value)
    }

    override fun encodeChar(value: Char) {
        container.set(key, PersistentDataType.STRING, value.toString())
    }

    override fun encodeDouble(value: Double) {
        container.set(key, PersistentDataType.DOUBLE, value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        container.set(key, PersistentDataType.INTEGER, index)
    }

    override fun encodeFloat(value: Float) {
        container.set(key, PersistentDataType.FLOAT, value)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder = this

    override fun encodeInt(value: Int) {
        container.set(key, PersistentDataType.INTEGER, value)
    }

    override fun encodeLong(value: Long) {
        container.set(key, PersistentDataType.LONG, value)
    }

    @ExperimentalSerializationApi
    override fun encodeNull() {
        container.remove(key)
    }

    override fun encodeShort(value: Short) {
        container.set(key, PersistentDataType.SHORT, value)
    }

    override fun encodeString(value: String) {
        container.set(key, PersistentDataType.STRING, value)
    }

    private fun encodeByteArray(value: ByteArray) {
        container.set(key, PersistentDataType.BYTE_ARRAY, value)
    }

    private val byteArrayDescriptor = serializer<ByteArray>().descriptor

    private fun encodeIntArray(value: IntArray) {
        container.set(key, PersistentDataType.INTEGER_ARRAY, value)
    }

    private val intArrayDescriptor = serializer<IntArray>().descriptor

    private fun encodeLongArray(value: LongArray) {
        container.set(key, PersistentDataType.LONG_ARRAY, value)
    }

    private val longArrayDescriptor = serializer<LongArray>().descriptor

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        when (serializer.descriptor) {
            byteArrayDescriptor -> encodeByteArray(value as ByteArray)
            intArrayDescriptor -> encodeIntArray(value as IntArray)
            longArrayDescriptor -> encodeLongArray(value as LongArray)
            else -> super.encodeSerializableValue(serializer, value)
        }
    }

    companion object {

        /**
         * Encodes a value to a [PersistentDataContainer] using the given [SerializationStrategy].
         *
         * @param strategy The strategy to use for encoding.
         * @param key The key to store the value under.
         * @param value The value to encode.
         * @param container The container to write to.
         * @param T The type of the value.
         */
        fun <T> encode(
            strategy: SerializationStrategy<T>,
            key: NamespacedKey,
            value: T,
            container: PersistentDataContainer
        ) {
            PersistentDataEncoder(key.plugin, container.adapterContext, key, container)
                .encodeSerializableValue(strategy, value)
        }
    }
}