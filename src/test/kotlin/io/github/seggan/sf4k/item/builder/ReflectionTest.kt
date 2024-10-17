package io.github.seggan.sf4k.item.builder

import io.github.seggan.sf4k.TestObject
import io.github.seggan.sf4k.util.findConstructor
import io.github.seggan.sf4k.util.findConstructorFromArgs
import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import org.bukkit.inventory.ItemStack
import strikt.api.expectThat
import strikt.assertions.isNotNull
import kotlin.test.Test

class ReflectionTest {

    @Suppress("unused")
    private class TestItem(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<out ItemStack>,
        val extra: Int
    ) : SlimefunItem(itemGroup, item, recipeType, recipe)

    @Test()
    fun testGetConstructors() {
        expectThat(
            TestItem::class.findConstructor(
                ItemGroup::class,
                SlimefunItemStack::class,
                RecipeType::class,
                Array<out ItemStack>::class,
                Int::class
            )
        ).isNotNull()
        expectThat(
            TestItem::class.findConstructor(
                ItemGroup::class,
                SlimefunItemStack::class,
                RecipeType::class,
                Array<out ItemStack>::class
            )
        ).isNotNull()
        expectThat(
            TestObject::class.findConstructorFromArgs("a", 1, null)
        ).isNotNull()
    }
}