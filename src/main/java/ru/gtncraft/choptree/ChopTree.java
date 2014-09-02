package ru.gtncraft.choptree;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChopTree extends JavaPlugin {
    @Override
    public void onEnable() {
        new BlockListener(this);
    }
}



