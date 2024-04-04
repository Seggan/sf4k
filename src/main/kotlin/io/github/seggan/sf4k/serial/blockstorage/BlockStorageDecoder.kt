package io.github.seggan.sf4k.serial.blockstorage

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
class BlockStorageDecoder private constructor(
    private val data: StringBuilder,
    private var elementsCount: Int,
) : AbstractDecoder() {

    override val serializersModule = EmptySerializersModule()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        if (elementsCount >= descriptor.elementsCount) return CompositeDecoder.DECODE_DONE
        return elementsCount++
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        BlockStorageDecoder(data, descriptor.elementsCount)

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

    companion object {
        fun <T> decode(strategy: DeserializationStrategy<T>, data: String): T {
            val decoder = BlockStorageDecoder(StringBuilder(data), 0)
            return decoder.decodeSerializableValue(strategy)
        }
    }
}