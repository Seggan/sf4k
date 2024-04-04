package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import java.util.WeakHashMap

class PersistentDataDecoder internal constructor(
    private val plugin: Plugin,
    private val key: NamespacedKey,
    private val container: PersistentDataContainer
) : Decoder {

    override val serializersModule = EmptySerializersModule()

    private fun <T> getOrThrow(type: PersistentDataType<*, T>): T {
        return container.get(key, type) ?: throw SerializationException("No value found for key $key")
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder {
        return PersistentDataCompositeDecoder(plugin, container.get(key, PersistentDataType.TAG_CONTAINER)!!)
    }

    override fun decodeBoolean(): Boolean = getOrThrow(PersistentDataType.BOOLEAN)

    override fun decodeByte(): Byte = getOrThrow(PersistentDataType.BYTE)

    override fun decodeChar(): Char = getOrThrow(PersistentDataType.STRING).first()

    override fun decodeDouble(): Double = getOrThrow(PersistentDataType.DOUBLE)

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeInt()

    override fun decodeFloat(): Float = getOrThrow(PersistentDataType.FLOAT)

    override fun decodeInline(descriptor: SerialDescriptor): Decoder = this

    override fun decodeInt(): Int = getOrThrow(PersistentDataType.INTEGER)

    override fun decodeLong(): Long = getOrThrow(PersistentDataType.LONG)

    @ExperimentalSerializationApi
    override fun decodeNotNullMark(): Boolean = key in container.keys

    @ExperimentalSerializationApi
    override fun decodeNull(): Nothing? = null

    override fun decodeShort(): Short = getOrThrow(PersistentDataType.SHORT)

    override fun decodeString(): String = getOrThrow(PersistentDataType.STRING)

    companion object {

        private val keyCache = WeakHashMap<NamespacedKey, Plugin>()

        fun <T> decode(
            strategy: DeserializationStrategy<T>,
            key: NamespacedKey,
            container: PersistentDataContainer
        ): T {
            val plugin = keyCache.getOrPut(key) {
                Bukkit.getPluginManager().plugins.find {
                    it.name.equals(key.namespace, ignoreCase = true)
                } ?: throw SerializationException("No plugin found for key $key")
            }
            return PersistentDataDecoder(plugin, key, container).decodeSerializableValue(strategy)
        }
    }
}