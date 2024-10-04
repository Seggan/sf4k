package io.github.seggan.sf4k.extensions

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import org.bukkit.inventory.ItemStack

/**
 * Checks if the item stack has slimefun data.
 */
fun ItemStack.isSlimefun() : Boolean = SlimefunItem.getByItem(this) != null

/**
 * Returns the associated SlimefunItem to an item stack,
 * in case none is found the return value will be null.
 */
fun ItemStack.getSlimefun() : SlimefunItem? = SlimefunItem.getByItem(this)

/**
 * Behaves the same way as the extension function [ItemStack.getSlimefun],
 * only difference is that it casts the result to a super class of a specified [SlimefunItem].
 */
inline fun <reified T : SlimefunItem> ItemStack.getSlimefun(): T {
    return this.getSlimefun() as T
}
