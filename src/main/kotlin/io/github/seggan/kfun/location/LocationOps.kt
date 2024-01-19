package io.github.seggan.kfun.location

import io.github.thebusybiscuit.slimefun4.libraries.dough.blocks.BlockPosition
import org.bukkit.Location

operator fun Location.component1(): Double = x
operator fun Location.component2(): Double = y
operator fun Location.component3(): Double = z

operator fun Location.plus(other: Location): Location = this.clone().add(other)
operator fun Location.plusAssign(other: Location) { this.add(other) }

operator fun Location.minus(other: Location): Location = this.clone().subtract(other)
operator fun Location.minusAssign(other: Location) { this.subtract(other) }

operator fun Location.times(factor: Double): Location = this.clone().multiply(factor)
operator fun Location.timesAssign(factor: Double) { this.multiply(factor) }

operator fun Location.div(factor: Double): Location = this.clone().multiply(1 / factor)
operator fun Location.divAssign(factor: Double) { this.multiply(1 / factor) }

val Location.position: BlockPosition
    get() = BlockPosition(this)