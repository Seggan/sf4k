package io.github.seggan.sf4k.serial.blockstorage

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.serializer
import me.mrCookieSlime.Slimefun.api.BlockStorage
import org.bukkit.Location
import org.bukkit.block.Block

inline fun <reified T> Location.getBlockStorage(
    key: String,
    strategy: DeserializationStrategy<T> = serializer()
): T? {
    val encoded = BlockStorage.getLocationInfo(this, key)
    return BlockStorageDecoder.decode(strategy, encoded)
}

inline fun <reified T> Block.getBlockStorage(
    key: String,
    strategy: DeserializationStrategy<T> = serializer()
): T? = location.getBlockStorage(key, strategy)

inline fun <reified T> Location.setBlockStorage(
    key: String,
    value: T,
    strategy: SerializationStrategy<T> = serializer()
) {
    val encoded = BlockStorageEncoder.encode(strategy, value)
    BlockStorage.addBlockInfo(this, key, encoded)
}

inline fun <reified T> Block.setBlockStorage(
    key: String,
    value: T,
    strategy: SerializationStrategy<T> = serializer()
) = location.setBlockStorage(key, value, strategy)

private const val BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"

internal fun Long.toBase62(): String {
    val sb = StringBuilder()
    var num = this
    while (num > 0) {
        sb.append(BASE62[num.toInt() % 62])
        num /= 62
    }
    return sb.reverse().toString()
}

internal fun CharSequence.fromBase62(): Long = fold(0) { acc, c -> acc * 62 + BASE62.indexOf(c) }

internal fun StringBuilder.nom(): Char {
    val c = this[0]
    deleteCharAt(0)
    return c
}

internal fun StringBuilder.nom(n: Int): String {
    val s = StringBuilder()
    repeat(n) {
        s.append(nom())
    }
    return s.toString()
}

internal inline fun StringBuilder.nomWhile(predicate: (Char) -> Boolean): String {
    val sb = StringBuilder()
    while (true) {
        val c = nom()
        if (predicate(c)) {
            sb.append(c)
        } else {
            break
        }
    }
    return sb.toString()
}