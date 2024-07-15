package io.github.seggan.sf4k.serial

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageDecoder
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageEncoder
import io.github.seggan.sf4k.serial.serializers.defaultSerializerOrRegistered
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun
import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition
import kotlinx.serialization.serializer
import org.bukkit.World
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import strikt.api.Assertion
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

class TestBSSerial {

    private lateinit var server: ServerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
        MockBukkit.load(Slimefun::class.java)
    }

    @Test
    fun testEncode() {
        1.invariantUnderSerialization()
        (-1).invariantUnderSerialization()
        1.0.invariantUnderSerialization()
        (-1.0).invariantUnderSerialization()
        "test".invariantUnderSerialization()
        'c'.invariantUnderSerialization()
        true.invariantUnderSerialization()
        false.invariantUnderSerialization()
        1.toByte().invariantUnderSerialization()
        1.toShort().invariantUnderSerialization()
        1L.invariantUnderSerialization()
        1F.invariantUnderSerialization()
        listOf(1, 2, 3).invariantUnderSerialization()
        mapOf(1 to 2, 3 to 4).invariantUnderSerialization()
        TestObject("a", 1, "c").invariantUnderSerialization()
        TestObject("a", 1, null).invariantUnderSerialization()
        TestValue("a").invariantUnderSerialization()
        listOf(TestObject("a", 1, "c"), TestObject("a", 1, null)).invariantUnderSerialization()
        mapOf("a" to TestObject("a", 1, "c"), "b" to TestObject("a", 1, null)).invariantUnderSerialization()

        val world = server.addSimpleWorld("world")
        BlockPosition(world, 53, 69, -9).invariantUnderSerialization()
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }
}

private inline fun <reified T> T.invariantUnderSerialization() {
    val encoded = BlockStorageEncoder.encode(defaultSerializerOrRegistered(), this)
    val decoded = BlockStorageDecoder.decode<T>(defaultSerializerOrRegistered(), encoded)
    println(encoded)
    expectThat(decoded).isEqualTo(this)
}

