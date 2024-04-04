package io.github.seggan.sf4k.serial

import io.github.seggan.sf4k.serial.blockstorage.BlockStorageDecoder
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageEncoder
import kotlinx.serialization.serializer
import kotlin.test.Test

class TestBSSerial {

    @Test
    fun testEncode() {
        TestObject("a", 1, "c").invariantUnderSerializing()
        listOf(TestObject("a", 1, "c"), TestObject("b", 2, "d")).invariantUnderSerializing()
        listOf(true, false, null).invariantUnderSerializing()
        mapOf("a" to 1, "b" to 2).invariantUnderSerializing()
    }
}

private inline fun <reified T> T.invariantUnderSerializing() {
    val encoded = BlockStorageEncoder.encode(serializer(), this)
    val decoded = BlockStorageDecoder.decode<T>(serializer(), encoded)
    println(encoded)
    assert(this == decoded)
}

