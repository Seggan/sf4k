package io.github.seggan.sf4k.item.builder

import io.github.seggan.sf4k.extensions.defaultLegacyColor
import io.github.seggan.sf4k.extensions.miniMessageToLegacy
import io.github.seggan.sf4k.util.RequiredProperty
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.inventory.ItemStack
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

/**
 * The main DSL class for constructing a [SlimefunItem]
 *
 * @param registry the [ItemRegistry] this builder is associated with
 *
 * @see buildSlimefunItem
 */
class ItemBuilder(private val registry: ItemRegistry) {

    /**
     * The [SlimefunItem]'s name, in [MiniMessage] format
     */
    var name: String by RequiredProperty()

    /**
     * The [SlimefunItem]'s [MaterialType]
     */
    var material: MaterialType by RequiredProperty()

    /**
     * The [SlimefunItem]'s ID. It will automatically be prefixed with the registry's
     * [ItemRegistry.prefix].
     */
    var id: String by RequiredProperty(setter = { "${registry.prefix}_${it.uppercase()}" })

    /**
     * The [SlimefunItem]'s [ItemGroup]
     */
    var category: ItemGroup by RequiredProperty()

    /**
     * The [SlimefunItem]'s [RecipeType]
     */
    var recipeType: RecipeType by RequiredProperty()

    /**
     * The [SlimefunItem]'s recipe
     */
    var recipe: Array<out ItemStack?> by RequiredProperty()

    private val lore = mutableListOf<String>()

    /**
     * Adds a line of lore, in [MiniMessage] format
     */
    operator fun String.unaryPlus() {
        lore += this.miniMessageToLegacy()
    }

    fun build(clazz: KClass<out SlimefunItem>, vararg otherArgs: Any?): SlimefunItemStack {
        val sfi = SlimefunItemStack(
            id,
            material.convert(),
            name.miniMessageToLegacy().defaultLegacyColor('f'),
            *lore.map { it.defaultLegacyColor('7') }.toTypedArray()
        )
        val constructor = clazz.primaryConstructor ?: error("Primary constructor not found for $clazz")
        constructor.call(category, sfi, recipeType, recipe, *otherArgs).register(registry.addon)
        return sfi
    }
}

/**
 * Builds a [SlimefunItem] of the specified type. The item **must** have a primary
 * constructor `(ItemGroup, SlimefunItemStack, RecipeType, Array<out ItemStack>, ...)`.
 * Any extra arguments given to this function will be passed to the end of the item's
 * constructor.
 *
 * Example:
 * ```kotlin
 * class MyItem(g: ItemGroup, i: SlimefunItemStack, t: RecipeType, r: Array<out ItemStack>, val something: Int)
 *  : SlimefunItem(g, i, t, r)
 *
 * // --- snip ---
 *
 * buildSlimefunItem<MyItem>(1) { // 1 will be passed to the `something` parameter
 *  category = ItemGroup.SOMETHING
 *  name = "<gray>The default item name is white, but you can change it with MiniMessage"
 *  id = "BAR"
 *  recipe = RecipeType.NULL
 *  recipe = arrayOf(...)
 *
 *  +"This is a line of lore!"
 *  +"<yellow><bold>The default color is gray but you can change it with MiniMessage"
 * }
 * ```
 *
 * @param otherArgs any arguments to be passed to the item's constructor
 * @param builder the block to build with
 *
 * @return the constructed [SlimefunItemStack], with the corresponding [SlimefunItem]
 *  already registered
 */
inline fun <reified I : SlimefunItem> ItemRegistry.buildSlimefunItem(
    vararg otherArgs: Any?,
    builder: ItemBuilder.() -> Unit
): SlimefunItemStack {
    return ItemBuilder(this).apply(builder).build(I::class, *otherArgs)
}

/**
 * Builds a default [SlimefunItem] in the same way as [buildSlimefunItem].
 *
 * @param builder the block to build with
 *
 * @return the constructed [SlimefunItemStack], with the corresponding [SlimefunItem]
 *  already registered
 *
 * @see buildSlimefunItem
 */
inline fun ItemRegistry.buildSlimefunItem(
    builder: ItemBuilder.() -> Unit
): SlimefunItemStack {
    return ItemBuilder(this).apply(builder).build(SlimefunItem::class)
}