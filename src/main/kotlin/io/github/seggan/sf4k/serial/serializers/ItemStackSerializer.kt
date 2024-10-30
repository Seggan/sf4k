package io.github.seggan.sf4k.serial.serializers

import kotlinx.serialization.serializer
import org.bukkit.Bukkit
import org.bukkit.configuration.serialization.ConfigurationSerializable
import org.bukkit.inventory.ItemStack
import org.bukkit.util.io.BukkitObjectInputStream
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

object ItemStackSerializer : DelegatingSerializer<ItemStack, ByteArray>(serializer()) {

    override fun toData(value: ItemStack): ByteArray {
        val bao = ByteArrayOutputStream()
        BukkitObjectOutputStream(bao).use { it.writeObject(value) }
        return bao.toByteArray()
    }

    override fun fromData(value: ByteArray): ItemStack {
        val bai = ByteArrayInputStream(value)
        return BukkitObjectInputStream(bai).use { it.readObject() as ItemStack }
    }
}