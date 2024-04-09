package io.github.seggan.sf4k.serial.serializers

import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition
import kotlinx.serialization.SerializationException
import kotlinx.serialization.builtins.LongArraySerializer
import org.bukkit.Bukkit
import java.util.*

object BlockPositionSerializer : DelegatingSerializer<BlockPosition, LongArray>(
    LongArraySerializer(),
    BlockPosition::class.java.simpleName
) {

    override fun toData(value: BlockPosition): LongArray {
        val world = value.world.uid
        val pos = value.position
        return longArrayOf(world.mostSignificantBits, world.leastSignificantBits, pos)
    }

    override fun fromData(value: LongArray): BlockPosition {
        val worldId = UUID(value[0], value[1])
        val world = Bukkit.getWorld(worldId) ?: throw SerializationException("World with ID $worldId not found")
        return BlockPosition(world, value[2])
    }
}