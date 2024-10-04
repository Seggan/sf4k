package io.github.seggan.sf4k.item

import io.github.thebusybiscuit.slimefun4.api.items.ItemGroup
import io.github.thebusybiscuit.slimefun4.api.items.ItemSetting
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack
import io.github.thebusybiscuit.slimefun4.api.recipes.RecipeType
import io.github.thebusybiscuit.slimefun4.core.handlers.*
import io.github.thebusybiscuit.slimefun4.implementation.handlers.SimpleBlockBreakHandler
import me.mrCookieSlime.CSCoreLibPlugin.Configuration.Config
import me.mrCookieSlime.Slimefun.Objects.handlers.BlockTicker
import org.bukkit.block.Block
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.inventory.ItemStack
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodHandles
import kotlin.properties.PropertyDelegateProvider
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.superclasses
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod
import io.github.thebusybiscuit.slimefun4.api.items.ItemHandler as SfItemHandler

/**
 * A "better" version of [SlimefunItem] that allows the use of [ItemHandler] and [Ticker] annotations.
 * Also comes with miscellaneous stuff.
 *
 * @see ItemHandler
 * @see Ticker
 */
open class BetterSlimefunItem : SlimefunItem {

    constructor(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<out ItemStack?>
    ) : super(itemGroup, item, recipeType, recipe)

    constructor(
        itemGroup: ItemGroup,
        item: SlimefunItemStack,
        recipeType: RecipeType,
        recipe: Array<out ItemStack?>,
        recipeOutput: ItemStack
    ) : super(itemGroup, item, recipeType, recipe, recipeOutput)

    constructor(
        itemGroup: ItemGroup,
        item: ItemStack,
        id: String,
        recipeType: RecipeType,
        recipe: Array<out ItemStack?>
    ) : super(itemGroup, item, id, recipeType, recipe)

    companion object {

        private val handlerMap =
            mutableMapOf<KClass<out SfItemHandler>, (List<MethodHandle>) -> SfItemHandler>()

        /**
         * Registers a custom handler for a given [SfItemHandler]
         *
         * @param clazz the class of the [SfItemHandler]
         * @param handler a function that takes in a list of [MethodHandle]s representing each handler
         *  and returns an [SfItemHandler]
         */
        fun registerHandler(
            clazz: KClass<out SfItemHandler>,
            handler: (List<MethodHandle>) -> SfItemHandler
        ) {
            handlerMap[clazz] = handler
        }

        init {
            registerHandler(BlockBreakHandler::class) { handles ->
                object : BlockBreakHandler(false, false) {
                    override fun onPlayerBreak(e: BlockBreakEvent, item: ItemStack, drops: MutableList<ItemStack>) {
                        handles.invokeAll(e, item, drops)
                    }
                }
            }
            registerHandler(BlockDispenseHandler::class) { BlockDispenseHandler(it::invokeAll) }
            registerHandler(BlockPlaceHandler::class) { handle ->
                object : BlockPlaceHandler(false) {
                    override fun onPlayerPlace(e: BlockPlaceEvent) {
                        handle.invokeAll(e)
                    }
                }
            }
            registerHandler(BlockUseHandler::class) { BlockUseHandler(it::invokeAll) }
            registerHandler(BowShootHandler::class) { BowShootHandler(it::invokeAll) }
            registerHandler(EntityInteractHandler::class) { EntityInteractHandler(it::invokeAll) }
            registerHandler(EntityKillHandler::class) { EntityKillHandler(it::invokeAll) }
            registerHandler(ItemConsumptionHandler::class) { ItemConsumptionHandler(it::invokeAll) }
            registerHandler(ItemDropHandler::class) {
                ItemDropHandler { e, p, i -> it.map { it.invoke(e, p, i) }.all { it as Boolean } }
            }
            registerHandler(ItemUseHandler::class) { ItemUseHandler(it::invokeAll) }
            registerHandler(MultiBlockInteractionHandler::class) {
                MultiBlockInteractionHandler { p, mb, b -> it.map { it.invoke(p, mb, b) }.all { it as Boolean } }
            }
            registerHandler(SimpleBlockBreakHandler::class) {
                object : SimpleBlockBreakHandler() {
                    override fun onBlockBreak(b: Block) {
                        it.invokeAll(b)
                    }
                }
            }
            registerHandler(ToolUseHandler::class) { ToolUseHandler(it::invokeAll) }
            registerHandler(WeaponUseHandler::class) { WeaponUseHandler(it::invokeAll) }
        }
    }

    final override fun preRegister() {
        val handlers = mutableMapOf<KClass<out SfItemHandler>, MutableSet<MethodHandle>>()
        val tickerMethods = mutableListOf<MethodHandle>()
        val tickerAsyncs = mutableListOf<Boolean>()
        for (method in this::class.getAllMethods()) {
            if (method.hasAnnotation<ItemHandler>()) {
                method.isAccessible = true
                val handler = method.findAnnotation<ItemHandler>()!!.handler
                val handle = MethodHandles.lookup().unreflect(method.javaMethod).bindTo(this)
                handlers.getOrPut(handler, ::mutableSetOf).add(handle)
            } else if (method.hasAnnotation<Ticker>()) {
                method.isAccessible = true
                val handle = MethodHandles.lookup().unreflect(method.javaMethod).bindTo(this)
                val async = method.findAnnotation<Ticker>()!!.async
                tickerMethods.add(handle)
                tickerAsyncs.add(async)
            }
        }

        for ((clazz, handler) in handlers) {
            val sfHandler = handlerMap[clazz]?.invoke(handler.toList()) ?: error("Handler not found for $clazz")
            addItemHandler(sfHandler)
        }

        if (tickerMethods.isNotEmpty()) {
            val isAsync = tickerAsyncs.first()
            if (tickerAsyncs.any { it != isAsync }) error("All tickers must be either sync or async")
            addItemHandler(object : BlockTicker() {

                val isSync = !isAsync

                override fun isSynchronized(): Boolean = isSync

                override fun tick(b: Block, item: SlimefunItem, data: Config) {
                    tickerMethods.invokeAll(b)
                }
            })
        }

        beforeRegister()
    }

    /**
     * Invoked before registration of the item
     */
    protected open fun beforeRegister() {}

    protected fun <T : Any> itemSetting(key: String, default: T): ItemSetting<T> {
        val setting = ItemSetting(this, key, default)
        addItemSetting(setting)
        return setting
    }

    /**
     * Creates an item setting using the property name as the key, converted from `camelCase`
     * to `kebab-case`
     */
    protected fun <T : Any> itemSetting(default: T) = PropertyDelegateProvider<Any?, ItemSetting<T>> { _, property ->
        itemSetting(property.name.camelCaseToKebabCase(), default)
    }
}

private fun KClass<*>.getAllMethods(): Set<KFunction<*>> {
    return members.filterIsInstanceTo<KFunction<*>, _>(mutableSetOf()) + superclasses.flatMap(KClass<*>::getAllMethods)
}

private fun List<MethodHandle>.invokeAll(vararg args: Any?) {
    val list = args.toList()
    for (handle in this) {
        handle.invokeWithArguments(list)
    }
}

private fun String.camelCaseToKebabCase(): String {
    val sb = StringBuilder()
    for (c in this) {
        if (c.isUpperCase()) {
            sb.append('-')
            sb.append(c.lowercase())
        } else {
            sb.append(c)
        }
    }
    return sb.toString()
}