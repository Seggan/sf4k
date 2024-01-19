package io.github.seggan.kfun.serial

import me.mrCookieSlime.Slimefun.api.BlockStorage
import org.bukkit.Location
import org.bukkit.block.Block

fun <T> Location.getBlockStorage(key: String, type: BlockStorageDataType<out T>): T? {
    return BlockStorage.getLocationInfo(this, key)?.let(type::deserialize)
}

fun <T> Block.getBlockStorage(key: String, type: BlockStorageDataType<out T>): T? =
    location.getBlockStorage(key, type)

fun <T> Location.setBlockStorage(key: String, value: T, type: BlockStorageDataType<in T>) {
    BlockStorage.addBlockInfo(this, key, type.serialize(value))
}

fun <T> Block.setBlockStorage(key: String, value: T, type: BlockStorageDataType<in T>) =
    location.setBlockStorage(key, value, type)