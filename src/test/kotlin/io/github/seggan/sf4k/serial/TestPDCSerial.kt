package io.github.seggan.sf4k.serial

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.github.seggan.sf4k.serial.pdc.get
import io.github.seggan.sf4k.serial.pdc.set
import io.github.seggan.sf4k.serial.serializers.LocationSerializer
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class TestPDCSerial {

    private lateinit var server: ServerMock

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
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
    }

    @Test
    fun testCustomSerializer() {
        val world = server.addSimpleWorld("world")
        Location(world, 0.0, 0.0, 0.0).invariantUnderSerialization(LocationSerializer)
    }

    private inline fun <reified T> T.invariantUnderSerialization(serializer: KSerializer<T> = serializer()) {
        val player = server.addPlayer()
        val testPlugin = MockBukkit.createMockPlugin()
        val key = NamespacedKey(testPlugin, "test")
        val pdc = player.persistentDataContainer
        pdc.set(key, this, serializer)
        val decoded: T = pdc.get(key, serializer)!!
        assertEquals(this, decoded)
        server.pluginManager.disablePlugin(testPlugin)
        server.setPlayers(0)
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }
}