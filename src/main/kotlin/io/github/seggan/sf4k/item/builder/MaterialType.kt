package io.github.seggan.sf4k.item.builder

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.utils.SlimefunUtils
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Represents the different types of material a [SlimefunItemStack] can have
 */
sealed interface MaterialType {

    /**
     * Converts this [MaterialType] into an [org.bukkit.inventory.ItemStack]
     */
    fun convert(): org.bukkit.inventory.ItemStack

    /**
     * A simple [org.bukkit.Material]
     */
    class Material(private val material: org.bukkit.Material) : MaterialType {
        override fun convert() = ItemStack(material)
    }

    /**
     * A full on [org.bukkit.inventory.ItemStack]
     */
    class ItemStack(private val itemStack: org.bukkit.inventory.ItemStack) : MaterialType {
        override fun convert() = itemStack
    }

    /**
     * A player head
     */
    class Head(private val texture: String) : MaterialType {
        override fun convert() = SlimefunUtils.getCustomHead(texture)
    }
}

fun Material.asMaterialType() = MaterialType.Material(this)
fun ItemStack.asMaterialType() = MaterialType.ItemStack(this)