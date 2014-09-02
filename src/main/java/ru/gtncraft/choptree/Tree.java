package ru.gtncraft.choptree;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.ArrayList;
import java.util.Collection;

final class Tree {

    final static Collection<BlockFace> directions = ImmutableList.of(
            BlockFace.UP,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    );
    final static int max = 1024;
    final Collection<Block> visited = new ArrayList<>();
    int leaves = 0;
    int call = 0;

    public Tree(final Block block) {
        get(block);
        if (isTree()) {
            destroy();
        }
    }

    void get(final Block block) {

        call++;

        if (call >= max) {
            return;
        }

        if (block.getType() == Material.AIR) {
            return;
        }

        if (visited.contains(block)) {
            return;
        }

        if (block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
            visited.add(block);
            for (BlockFace direction : directions) {
                get(block.getRelative(direction));
            }
            return;
        }

        if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2) {
            leaves++;
        }
    }

    boolean isTree() {
        return leaves > 1;
    }

    void destroy() {
        visited.forEach(Block::breakNaturally);
    }
}
