package io.github.seggan.sf4k.extensions

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import org.bukkit.inventory.ItemStack

fun ItemStack.isSlimefun() = SlimefunItem.getByItem(this) != null
fun ItemStack.asSlimefun() = SlimefunItem.getByItem(this)

inline fun <reified T : SlimefunItem> ItemStack.asSlimefun(): T {
    return this.asSlimefun() as T
}
