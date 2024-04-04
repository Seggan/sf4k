package io.github.seggan.sf4k.serial

import io.github.seggan.sf4k.serial.blockstorage.BlockStorageDecoder
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageEncoder
import kotlinx.serialization.Serializable
import kotlin.test.Test

class TestBSSerial {

    @Test
    fun testEncode() {
        TestObject("a", 1, "c").invariantUnderSerializing()
        listOf(TestObject("a", 1, "c"), TestObject("b", 2, "d")).invariantUnderSerializing()
        listOf(true, false, null).invariantUnderSerializing()
    }
}

private inline fun <reified T> T.invariantUnderSerializing() {
    val encoded = BlockStorageEncoder.encode(this)
    val decoded = BlockStorageDecoder.decode<T>(encoded)
    println(encoded)
    assert(this == decoded)
}

@Serializable
private data class TestObject(val a: String, val b: Int, val c: String?)