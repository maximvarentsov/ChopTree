package ru.gtncraft.choptree;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

final class BlockListener implements Listener {

    public BlockListener(final ChopTree instance) {
        Bukkit.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler(ignoreCancelled = true)
    @SuppressWarnings("unused")
    void onBlockBreak(final BlockBreakEvent event) {
        Block block = event.getBlock();
        if (isLog(block)) {
            new Tree(block);
        }		
    }

    boolean isLog(final Block block) {
        return block.getType() == Material.LOG || block.getType() == Material.LOG_2;
    }
}
