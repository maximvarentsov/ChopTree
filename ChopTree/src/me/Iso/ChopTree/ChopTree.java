package me.Iso.ChopTree;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ChopTree extends JavaPlugin {
    
    private final ChopTreeBlockListener blockListener = new ChopTreeBlockListener(this);
    private final ChopTreePlayerListener playerListener = new ChopTreePlayerListener(this);
    private final ChopTreeFiles files = new ChopTreeFiles(this);
    public final List <String> options = new LinkedList <String> ();
    public final List <String> chunks = new LinkedList <String> ();
    public final List <Integer> mats = new LinkedList <Integer> ();
    public final List <Player> chunkmsg = new LinkedList <Player> ();
    public final HashMap<String, Boolean> players = new HashMap<String, Boolean> ();
    public final HashMap<Player, Block[]> trees = new HashMap<Player, Block[]> ();
	
    @Override
    public void onEnable() {
    	PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(blockListener, this);
        pm.registerEvents(playerListener, this);
        files.initFile();
        files.initPlayers();
        files.initChunks();
    }
    
    @Override
    public void onDisable() {
        if (files.writeFile()) {
            getLogger().info("Saved changes.");
        } else {
            getLogger().warning("Could not save changes.");
        }
        files.writePlayers();
        files.writeChunks();
    }
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("ChopTree") || commandLabel.equalsIgnoreCase("ct")) {
            if (Array.getLength(args) == 0) {
                files.listOption(-1, sender);
            } else {
                if (args[0].toLowerCase().contains("reload") || args[0].equalsIgnoreCase("r")) {
                    options.clear();
                    mats.clear();
                    files.initFile();
                    files.listOption(-1, sender);
                    sender.sendMessage(ChatColor.DARK_AQUA + "Reloaded settings from properties file.");
                } else if (files.getOptionIndex(args[0]) != -1) {
                    if (Array.getLength(args) > 1) {
                        files.toggleOption(args[0], args[1]);
                    }
                    files.listOption(files.getOptionIndex(args[0]), sender);
                } else {
                    sender.sendMessage(ChatColor.RED + "That option doesn't exist.");
                }
            }
            return true;
        }
        return false;
    }
}
