package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.modules.EmptySerializersModule
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

class PersistentDataEncoder @PublishedApi internal constructor(
    private val plugin: Plugin,
    private val context: PersistentDataAdapterContext,
    internal val key: NamespacedKey,
    internal val container: PersistentDataContainer
) : Encoder {

    override val serializersModule = EmptySerializersModule()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder {
        return PersistentDataCompositeEncoder(plugin, context, this)
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
        container.set(key, PersistentDataType.STRING, enumDescriptor.getElementName(index))
    }

    override fun encodeFloat(value: Float) {
        container.set(key, PersistentDataType.FLOAT, value)
    }

    override fun encodeInline(descriptor: SerialDescriptor): Encoder {
        return this
    }

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
}