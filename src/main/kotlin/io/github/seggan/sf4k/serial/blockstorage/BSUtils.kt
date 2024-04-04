package io.github.seggan.sf4k.serial.blockstorage

import me.mrCookieSlime.Slimefun.api.BlockStorage
import org.bukkit.Location
import org.bukkit.block.Block

inline fun <reified T> Location.getBlockStorage(key: String): T? {
    val encoded = BlockStorage.getLocationInfo(this, key)
    return BlockStorageDecoder.decode<T>(encoded)
}

inline fun <reified T> Block.getBlockStorage(key: String): T? = location.getBlockStorage(key)

inline fun <reified T> Location.setBlockStorage(key: String, value: T) {
    val encoded = BlockStorageEncoder.encode<T>(value)
    BlockStorage.addBlockInfo(this, key, encoded)
}

inline fun <reified T> Block.setBlockStorage(key: String, value: T) = location.setBlockStorage(key, value)