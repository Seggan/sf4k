package io.github.seggan.sf4k.serial

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import io.github.seggan.sf4k.serial.pdc.set
import org.bukkit.NamespacedKey
import org.junit.jupiter.api.AfterEach
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
        val player = server.addPlayer()
        val testPlugin = MockBukkit.createMockPlugin()
        val pdc = player.persistentDataContainer
        pdc.set(NamespacedKey(testPlugin, "test"), TestObject("a", 1, "c"))
        println(pdc)
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }
}