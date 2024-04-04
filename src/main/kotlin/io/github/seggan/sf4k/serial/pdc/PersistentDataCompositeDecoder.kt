package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

@OptIn(ExperimentalSerializationApi::class)
internal class PersistentDataCompositeDecoder(
    private val plugin: Plugin,
    private val data: PersistentDataContainer
) : CompositeDecoder {

    override val serializersModule = EmptySerializersModule()

    private var index = 0

    private fun SerialDescriptor.key(index: Int): NamespacedKey =
        NamespacedKey(plugin, this.getElementName(index))

    override fun decodeBooleanElement(descriptor: SerialDescriptor, index: Int): Boolean {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeBoolean()
    }

    override fun decodeByteElement(descriptor: SerialDescriptor, index: Int): Byte {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeByte()
    }

    override fun decodeCharElement(descriptor: SerialDescriptor, index: Int): Char {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeChar()
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int {
        return data.get(NamespacedKey(plugin, "size"), PersistentDataType.INTEGER) ?: 0
    }

    override fun decodeDoubleElement(descriptor: SerialDescriptor, index: Int): Double {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeDouble()
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (index >= descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return index++
    }

    override fun decodeFloatElement(descriptor: SerialDescriptor, index: Int): Float {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeFloat()
    }

    override fun decodeInlineElement(descriptor: SerialDescriptor, index: Int): Decoder {
        return PersistentDataDecoder(plugin, descriptor.key(index), data)
    }

    override fun decodeIntElement(descriptor: SerialDescriptor, index: Int): Int {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeInt()
    }

    override fun decodeLongElement(descriptor: SerialDescriptor, index: Int): Long {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeLong()
    }

    @ExperimentalSerializationApi
    override fun <T : Any> decodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T?>,
        previousValue: T?
    ): T? {
        val key = descriptor.key(index)
        return if (key in data.keys) {
            PersistentDataDecoder(plugin, key, data).decodeSerializableValue(deserializer)
        } else {
            null
        }
    }

    @ExperimentalSerializationApi
    override fun decodeSequentially(): Boolean = true

    override fun <T> decodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        deserializer: DeserializationStrategy<T>,
        previousValue: T?
    ): T {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeSerializableValue(deserializer)
    }

    override fun decodeShortElement(descriptor: SerialDescriptor, index: Int): Short {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeShort()
    }

    override fun decodeStringElement(descriptor: SerialDescriptor, index: Int): String {
        return PersistentDataDecoder(plugin, descriptor.key(index), data).decodeString()
    }

    override fun endStructure(descriptor: SerialDescriptor) {
    }
}