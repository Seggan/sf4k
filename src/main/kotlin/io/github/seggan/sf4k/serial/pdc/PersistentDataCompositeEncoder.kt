package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

@OptIn(ExperimentalSerializationApi::class)
internal class PersistentDataCompositeEncoder(
    private val plugin: Plugin,
    private val context: PersistentDataAdapterContext,
    private val parent: PersistentDataEncoder? = null
) : CompositeEncoder {

    override val serializersModule = EmptySerializersModule()

    private val data = context.newPersistentDataContainer()

    private fun SerialDescriptor.key(index: Int): NamespacedKey =
        NamespacedKey(plugin, this.getElementName(index))

    internal fun encodeSize(size: Int) {
        data.set(NamespacedKey(plugin, "size"), PersistentDataType.INTEGER, size)
    }

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeBoolean(value)
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeByte(value)
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeChar(value)
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeDouble(value)
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeFloat(value)
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        return PersistentDataEncoder(plugin, context, descriptor.key(index), data)
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeInt(value)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeLong(value)
    }

    @ExperimentalSerializationApi
    override fun <T : Any> encodeNullableSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T?
    ) {
        if (value == null) {
            data.remove(descriptor.key(index))
            return
        }
        encodeSerializableElement(descriptor, index, serializer, value)
    }

    override fun <T> encodeSerializableElement(
        descriptor: SerialDescriptor,
        index: Int,
        serializer: SerializationStrategy<T>,
        value: T
    ) {
        val encoder = PersistentDataEncoder(plugin, context, descriptor.key(index), data)
        serializer.serialize(encoder, value)
    }

    override fun encodeShortElement(descriptor: SerialDescriptor, index: Int, value: Short) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeShort(value)
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        PersistentDataEncoder(plugin, context, descriptor.key(index), data).encodeString(value)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        parent?.container?.set(parent.key, PersistentDataType.TAG_CONTAINER, data)
    }
}