package io.github.seggan.sf4k.serial

import be.seeseemelk.mockbukkit.MockBukkit
import be.seeseemelk.mockbukkit.ServerMock
import be.seeseemelk.mockbukkit.UnimplementedOperationException
import io.github.seggan.sf4k.TestObject
import io.github.seggan.sf4k.TestValue
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageDecoder
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageEncoder
import io.github.seggan.sf4k.serial.blockstorage.BlockStorageSettings
import io.github.seggan.sf4k.serial.pdc.getData
import io.github.seggan.sf4k.serial.pdc.setData
import io.github.seggan.sf4k.serial.serializers.BukkitSerializerRegistry
import kotlinx.serialization.KSerializer
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import strikt.api.expectThat
import strikt.assertions.isEqualTo
import kotlin.test.Test

class SerialTest {

    private lateinit var server: ServerMock
    private var testMode: TestMode? = null

    @BeforeEach
    fun setUp() {
        server = MockBukkit.mock()
    }

    @Test
    fun testBlockStorage() {
        testMode = TestMode.BLOCK_STORAGE
        encodeTests()
        testMode = null
    }

    @Test
    fun testPDC() {
        testMode = TestMode.PDC
        encodeTests()
        testMode = null
    }

    private fun encodeTests() {
        Unit.invariantUnderSerialization()
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
        NamespacedKey("test", "test").invariantUnderSerialization()
    }

    private inline fun <reified T> T.invariantUnderSerialization(serializer: KSerializer<T> = BukkitSerializerRegistry.serializer<T>()) {
        try {
            when (testMode) {
                TestMode.PDC -> {
                    val player = server.addPlayer()
                    val testPlugin = MockBukkit.createMockPlugin()
                    val key = NamespacedKey(testPlugin, "test")
                    val pdc = player.persistentDataContainer
                    pdc.setData(key, this, serializer)
                    val decoded: T = pdc.getData(key, serializer)!!
                    expectThat(this).isEqualTo(decoded)
                    server.pluginManager.disablePlugin(testPlugin)
                    server.setPlayers(0)
                }

                TestMode.BLOCK_STORAGE -> {
                    val encoded = BlockStorageEncoder.encode(
                        BukkitSerializerRegistry.serializer<T>(),
                        this,
                        BlockStorageSettings()
                    )
                    val decoded = BlockStorageDecoder.decode(
                        BukkitSerializerRegistry.serializer<T>(),
                        encoded,
                        BlockStorageSettings()
                    )
                    expectThat(decoded).isEqualTo(this)
                }

                null -> throw IllegalStateException("Test mode not set")
            }
        } catch (e: UnimplementedOperationException) {
            throw AssertionError(e)
        }
    }

    @AfterEach
    fun tearDown() {
        MockBukkit.unmock()
    }

    enum class TestMode {
        PDC,
        BLOCK_STORAGE
    }
}