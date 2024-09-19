package io.github.seggan.sf4k.extensions

import org.bukkit.util.Vector

operator fun Vector.component1() = x
operator fun Vector.component2() = y
operator fun Vector.component3() = z

operator fun Vector.plus(other: Vector) = clone().add(other)
operator fun Vector.plusAssign(other: Vector) { add(other) }

operator fun Vector.minus(other: Vector) = clone().subtract(other)
operator fun Vector.minusAssign(other: Vector) { subtract(other) }

operator fun Vector.times(other: Vector) = clone().multiply(other)
operator fun Vector.timesAssign(other: Vector) { multiply(other) }

operator fun Vector.times(other: Double) = clone().multiply(other)
operator fun Vector.timesAssign(other: Double) { multiply(other) }

operator fun Vector.times(other: Int) = clone().multiply(other)
operator fun Vector.timesAssign(other: Int) { multiply(other) }

operator fun Vector.div(other: Vector) = clone().divide(other)
operator fun Vector.divAssign(other: Vector) { divide(other) }

operator fun Vector.div(other: Double) = clone().multiply(1 / other) // WHY VECTOR NO HAVE DIVIDE AAAAA
operator fun Vector.divAssign(other: Double) { multiply(1 / other) }

operator fun Vector.div(other: Int) = clone().multiply(1.0 / other)
operator fun Vector.divAssign(other: Int) { multiply(1.0 / other) }