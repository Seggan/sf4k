package io.github.seggan.sf4k.serial.blockstorage

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.serializer

@OptIn(ExperimentalSerializationApi::class)
class BlockStorageEncoder private constructor() : AbstractEncoder() {

    override val serializersModule = EmptySerializersModule()

    private val data = StringBuilder()

    override fun encodeNull() {
        data.append('?')
    }

    @ExperimentalSerializationApi
    override fun encodeNotNullMark() {
        data.append('!')
    }

    override fun encodeValue(value: Any) {
        data.append(value.toString()).append(' ')
    }

    override fun encodeString(value: String) {
        data.append(value.length.toLong().toBase62()).append(' ').append(value)
    }

    override fun encodeBoolean(value: Boolean) {
        data.append(if (value) 't' else 'f')
    }

    override fun encodeByte(value: Byte) = encodeLong(value.toLong())
    override fun encodeInt(value: Int) = encodeLong(value.toLong())
    override fun encodeShort(value: Short) = encodeLong(value.toLong())
    override fun encodeLong(value: Long) {
        data.append(value.toBase62()).append(' ')
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) = encodeInt(index)

    override fun beginCollection(descriptor: SerialDescriptor, collectionSize: Int): CompositeEncoder {
        data.append(collectionSize.toLong().toBase62()).append(' ')
        return this
    }

    companion object {
        fun <T> encode(strategy: SerializationStrategy<T>, value: T): String {
            val encoder = BlockStorageEncoder()
            encoder.encodeSerializableValue(strategy, value)
            return encoder.data.toString()
        }
    }
}