package io.github.seggan.sf4k.item.builder

import it.unimi.dsi.fastutil.chars.Char2ObjectOpenHashMap
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Builds a size * size recipe
 *
 * @see buildRecipe
 */
class RecipeBuilder(private val size: Int) {

    init {
        require(size > 1) { "Recipe size must be greater than 1" }
    }

    private val recipe: Array<String> = Array(size) { " ".repeat(size) }
    private var row = 0

    private val charMap = Char2ObjectOpenHashMap<ItemStack?>().apply {
        put(' ', null)
    }

    /**
     * Adds a recipe row
     */
    operator fun String.unaryPlus() {
        require(length == size) { "Recipe must be ${size}x${size}" }
        require(row < size) { "Recipe must be ${size}x${size}" }
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
 * Builds a size * size recipe as an [Array] of [ItemStack]s.
 * Default to 3*3.
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
@OptIn(ExperimentalContracts::class)
inline fun buildRecipe(size: Int = 3, init: RecipeBuilder.() -> Unit): Array<out ItemStack?> {
    contract {
        callsInPlace(init, InvocationKind.EXACTLY_ONCE)
    }
    return RecipeBuilder(size).apply(init).build()
}
