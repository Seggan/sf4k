package io.github.seggan.sf4k.extensions

import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KProperty

/**
 * Checks if the [ItemStack] has Slimefun data.
 */
fun ItemStack.isSlimefun() : Boolean = SlimefunItem.getByItem(this) != null

/**
 * Returns the associated [SlimefunItem] to an item stack, cast to the specified subclass.
 * In case none is found, the return value will be null.
 */
inline fun <reified T : SlimefunItem> ItemStack?.getSlimefun(): T? {
    return SlimefunItem.getByItem(this) as? T
}

/**
 * Allows [ItemSetting] to be used as a delegate
 */
operator fun <T> ItemSetting<T>.getValue(thisRef: Any?, property: KProperty<*>): T = value
