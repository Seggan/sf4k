package io.github.seggan.kfun;

import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * An abstract class that is a child of both {@link JavaPlugin} and {@link SlimefunAddon}.
 * The reason this exists is that Kotlin does not like an interface giving a default
 * implementation of a method from a parent class, so this class is used to get around that.
 */
public abstract class AbstractAddon extends JavaPlugin implements SlimefunAddon {
}
