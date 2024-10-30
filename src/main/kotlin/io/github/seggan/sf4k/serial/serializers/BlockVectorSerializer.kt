package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.builtins.IntArraySerializer
import org.bukkit.util.BlockVector

object BlockVectorSerializer : DelegatingSerializer<BlockVector, IntArray>(IntArraySerializer()) {
    override fun toData(value: BlockVector): IntArray = intArrayOf(value.blockX, value.blockY, value.blockZ)
    override fun fromData(value: IntArray): BlockVector = BlockVector(value[0], value[1], value[2])
}