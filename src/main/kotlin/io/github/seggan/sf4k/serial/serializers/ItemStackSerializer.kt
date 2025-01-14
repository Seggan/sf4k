package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.serializer
import org.bukkit.inventory.ItemStack

object ItemStackSerializer : DelegatingSerializer<ItemStack, ByteArray>(serializer()) {

    override fun toData(value: ItemStack): ByteArray {
        return value.serializeAsBytes()
    }

    override fun fromData(value: ByteArray): ItemStack {
        return ItemStack.deserializeBytes(value)
    }
}