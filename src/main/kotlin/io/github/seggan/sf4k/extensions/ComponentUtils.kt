package io.github.seggan.sf4k.extensions

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.util.EnumSet

/**
 * Applies a default color to a legacy color string if none exists.
 * For example, `"&7Text".defaultLegacyColor('f')` remains unchanged, but
 * `"Text".defaultLegacyColor('f')` gets converted to `"&fText"`.
 */
fun String.defaultLegacyColor(color: Char): String {
    if (startsWith('&') || isBlank()) return this
    return "&$color$this"
}

/**
 * Converts a MiniMessage string to the legacy ampersand format. For example,
 * `"<white>Text".miniMessageToLegacy()` gets converted to `"&fText"`.
 */
fun String.miniMessageToLegacy(): String = LegacyComponentSerializer.legacyAmpersand()
    .serialize(MiniMessage.miniMessage().deserialize(this))

/**
 * Allows for a quick shortcut to create a colored string.
 * For example, `NamedTextColor.RED + "WARNING"` is equivalent to the legacy
 * string `"&cWARNING"`.
 */
operator fun TextColor.plus(s: String): TextComponent = Component.text()
    .color(this)
    .noDecorations()
    .content(s)
    .build()

/**
 * Sets the current [TextDecoration]s in the [TextComponent.Builder] to none.
 */
fun TextComponent.Builder.noDecorations(): TextComponent.Builder =
    decorations(EnumSet.allOf(TextDecoration::class.java), false)