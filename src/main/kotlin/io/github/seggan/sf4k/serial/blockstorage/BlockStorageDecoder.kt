package io.github.seggan.sf4k.serial.blockstorage

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalSerializationApi::class)
class BlockStorageDecoder private constructor(
    private val data: StringBuilder,
    private var elementsCount: Int,
    private val settings: BlockStorageSettings
) : AbstractDecoder() {

    override val serializersModule = EmptySerializersModule()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementsCount >= descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementsCount++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        BlockStorageDecoder(data, descriptor.elementsCount, settings)

    @ExperimentalSerializationApi
    override fun decodeSequentially(): Boolean = true

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int = decodeInt()

    override fun decodeBoolean(): Boolean = data.nom() == 't'

    override fun decodeByte(): Byte = decodeLong().toByte()

    override fun decodeChar(): Char = data.nom()

    override fun decodeDouble(): Double = data.nomWhile { it != ' ' }.toDouble()

    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int = decodeInt()

    override fun decodeFloat(): Float = data.nomWhile { it != ' ' }.toFloat()

    override fun decodeInt(): Int = decodeLong().toInt()

    override fun decodeLong(): Long = data.nomWhile { it != ' ' }.fromBase62()

    override fun decodeNotNullMark(): Boolean = data.nom() == '!'

    override fun decodeShort(): Short = decodeInt().toShort()

    override fun decodeString(): String = data.nom(decodeInt())

    private val byteArrayDescriptor = serializer<ByteArray>().descriptor

    @OptIn(ExperimentalEncodingApi::class)
    private fun decodeByteArray(): ByteArray {
        val base64 = data.nomWhile { it != ' ' }
        return Base64.decode(base64)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T {
        return if (settings.encodeByteArrayAsBase64 && deserializer.descriptor == byteArrayDescriptor) {
            decodeByteArray() as T
        } else {
            super.decodeSerializableValue(deserializer)
        }
    }

    companion object {

        /**
         * Decodes a value using the [BlockStorageDecoder].
         *
         * @param strategy The deserialization strategy to use.
         * @param data The data to decode.
         * @param settings The settings to use when deserializing the value.
         * @param T The type of the value.
         * @return The decoded value.
         */
        fun <T> decode(strategy: DeserializationStrategy<T>, data: String, settings: BlockStorageSettings): T {
            val decoder = BlockStorageDecoder(StringBuilder(data), 0, settings)
            return decoder.decodeSerializableValue(strategy)
        }
    }
}