package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.builtins.LongArraySerializer
import java.util.*

object UUIDSerializer : DelegatingSerializer<UUID, LongArray>(
    LongArraySerializer(),
    "java.util.UUID"
) {
    override fun toData(value: UUID): LongArray = longArrayOf(value.mostSignificantBits, value.leastSignificantBits)

    override fun fromData(value: LongArray): UUID = UUID(value[0], value[1])
}