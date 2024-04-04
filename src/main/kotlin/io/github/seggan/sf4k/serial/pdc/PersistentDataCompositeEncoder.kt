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
class PersistentDataCompositeEncoder(
    private val plugin: Plugin,
    private val context: PersistentDataAdapterContext,
    private val parent: PersistentDataEncoder? = null
) : CompositeEncoder {

    override val serializersModule = EmptySerializersModule()

    private val data = context.newPersistentDataContainer()

    private fun String.key(): NamespacedKey = NamespacedKey(plugin, this)

    private fun SerialDescriptor.key(index: Int): NamespacedKey = this.getElementName(index).key()

    override fun encodeBooleanElement(descriptor: SerialDescriptor, index: Int, value: Boolean) {
        data.set(descriptor.key(index), PersistentDataType.BOOLEAN, value)
    }

    override fun encodeByteElement(descriptor: SerialDescriptor, index: Int, value: Byte) {
        data.set(descriptor.key(index), PersistentDataType.BYTE, value)
    }

    override fun encodeCharElement(descriptor: SerialDescriptor, index: Int, value: Char) {
        data.set(descriptor.key(index), PersistentDataType.STRING, value.toString())
    }

    override fun encodeDoubleElement(descriptor: SerialDescriptor, index: Int, value: Double) {
        data.set(descriptor.key(index), PersistentDataType.DOUBLE, value)
    }

    override fun encodeFloatElement(descriptor: SerialDescriptor, index: Int, value: Float) {
        data.set(descriptor.key(index), PersistentDataType.FLOAT, value)
    }

    override fun encodeInlineElement(descriptor: SerialDescriptor, index: Int): Encoder {
        return PersistentDataEncoder(plugin, context, descriptor.key(index), data)
    }

    override fun encodeIntElement(descriptor: SerialDescriptor, index: Int, value: Int) {
        data.set(descriptor.key(index), PersistentDataType.INTEGER, value)
    }

    override fun encodeLongElement(descriptor: SerialDescriptor, index: Int, value: Long) {
        data.set(descriptor.key(index), PersistentDataType.LONG, value)
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
        data.set(descriptor.key(index), PersistentDataType.SHORT, value)
    }

    override fun encodeStringElement(descriptor: SerialDescriptor, index: Int, value: String) {
        data.set(descriptor.key(index), PersistentDataType.STRING, value)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        parent?.container?.set(parent.key, PersistentDataType.TAG_CONTAINER, data)
    }
}