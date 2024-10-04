package io.github.seggan.sf4k.item.builder

import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

/**
 * Builds a 3x3 recipe
 *
 * @see buildRecipe
 */
class RecipeBuilder {

    private val recipe: Array<String> = Array(3) { " ".repeat(3) }
    private var row = 0

    private val charMap = Char2ObjectOpenHashMap<ItemStack?>().apply {
        put(' ', null)
    }

    /**
     * Adds a recipe row
     */
    operator fun String.unaryPlus() {
        require(length == 3) { "Recipe must be 3x3" }
        require(row < 3) { "Recipe must be 3x3" }
        recipe[row++] = this
    }

    /**
     * Specifies that the given character means a certain [ItemStack]
     */
    infix fun Char.means(item: ItemStack?) {
        charMap.put(this, item)
    }

    /**
     * Specifies that the given character means a certain [Material]
     */
    infix fun Char.means(material: Material) {
        charMap.put(this, ItemStack(material))
    }

    fun build(): Array<out ItemStack?> {
        return recipe.flatMap { row -> row.map { charMap[it] } }.toTypedArray()
    }
}

/**
 * Builds a 3x3 recipe as an [Array] of [ItemStack]s
 *
 * Example:
 * ```kotlin
 * val recipe = buildRecipe {
 *  +" x "
 *  +" x "
 *  +" y "
 *
 *  'x' means Material.IRON_INGOT
 *  'y' means ItemStack(Material.STICK)
 * }
 * ```
 */
inline fun buildRecipe(init: RecipeBuilder.() -> Unit): Array<out ItemStack?> {
    return RecipeBuilder().apply(init).build()
}