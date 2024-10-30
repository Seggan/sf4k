package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.builtins.DoubleArraySerializer
import org.bukkit.util.Vector

object VectorSerializer : DelegatingSerializer<Vector, DoubleArray>(DoubleArraySerializer()) {
    override fun toData(value: Vector): DoubleArray = doubleArrayOf(value.x, value.y, value.z)
    override fun fromData(value: DoubleArray): Vector = Vector(value[0], value[1], value[2])
}