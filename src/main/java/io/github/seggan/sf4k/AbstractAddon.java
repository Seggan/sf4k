package io.github.seggan.sf4k;

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin;
import io.github.thebusybiscuit.slimefun4.api.SlimefunAddon;

/**
 * An abstract class that is a child of both {@link SuspendingJavaPlugin} and {@link SlimefunAddon}.
 * The reason this exists is that Kotlin does not like an interface giving a default
 * implementation of a method from a parent class, so this class is used to get around that.
 */
public abstract class AbstractAddon extends SuspendingJavaPlugin implements SlimefunAddon {

    @Override
    public final void onEnable() {
        super.onEnable();
    }

    @Override
    public final void onDisable() {
        super.onDisable();
    }

    @Override
    public final void onLoad() {
        super.onLoad();
    }
}
