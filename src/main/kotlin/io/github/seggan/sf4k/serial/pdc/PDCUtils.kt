package io.github.seggan.sf4k.serial.pdc

import kotlinx.serialization.serializer
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.persistence.PersistentDataContainer

inline fun <reified T> PersistentDataContainer.set(key: NamespacedKey, value: T) {
    val encoder = PersistentDataEncoder(
        Bukkit.getPluginManager().plugins.first { it.name.equals(key.namespace, ignoreCase = true) },
        adapterContext,
        key,
        this
    )
    encoder.encodeSerializableValue(serializer(), value)
}